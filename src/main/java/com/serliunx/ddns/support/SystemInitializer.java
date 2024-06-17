package com.serliunx.ddns.support;

import com.serliunx.ddns.config.Configuration;
import com.serliunx.ddns.constant.SystemConstants;
import com.serliunx.ddns.core.Clearable;
import com.serliunx.ddns.core.Refreshable;
import com.serliunx.ddns.core.context.MultipleSourceInstanceContext;
import com.serliunx.ddns.core.instance.Instance;
import com.serliunx.ddns.support.feign.client.IPAddressClient;
import com.serliunx.ddns.support.feign.client.entity.IPAddressResponse;
import com.serliunx.ddns.thread.TaskThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.serliunx.ddns.config.ConfigurationKeys.KEY_TASK_REFRESH_INTERVAL_IP;
import static com.serliunx.ddns.config.ConfigurationKeys.KEY_THREAD_POOL_CORE_SIZE;

/**
 * 系统初始化
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @version 1.0.0
 * @since 2024/5/15
 */
public final class SystemInitializer implements Refreshable, Clearable {

    private static final Logger log = LoggerFactory.getLogger(SystemInitializer.class);

    private final Configuration configuration;
    private final MultipleSourceInstanceContext instanceContext;

    private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;
    private Set<Instance> instances;
    private final Map<String, ScheduledFuture<?>> runningInstances = new HashMap<>(64);

    SystemInitializer(Configuration configuration, MultipleSourceInstanceContext instanceContext) {
        this.configuration = configuration;
        this.instanceContext = instanceContext;
    }

    public static Configurer configurer() {
        return new Configurer();
    }

    @Override
    public void refresh() {
        InstanceContextHolder.setAdditional("main-refreshing");
        log.info("程序正在初始化, 请稍候.");

        // 检查正在运行的实例信息, 安全的停止(手动刷新时需要执行的逻辑, 初始化不需要)
        checkAndCloseSafely();

        // 释放配置文件
        releaseResource(SystemConstants.CONFIG_PROPERTIES_FILE);

        // 刷新配置信息
        configuration.refresh();

        // 获取核心线程数量, 默认为CPU核心数量
        int coreSize = configuration.getInteger(KEY_THREAD_POOL_CORE_SIZE, Runtime.getRuntime().availableProcessors());

        // 初始化线程池
        initThreadPool(coreSize);

        // 加载实例(不同的容器加载时机不同)
        loadInstances();

        // 运行实例
        runInstances();

        // 实例提交后, 清理实例、配置缓存, 因为读取一次就不需要了
        clear();
        log.info("初始化完成!");
        InstanceContextHolder.clearAdditional();
    }

    @Override
    public void clear() {
        instanceContext.clear();
    }

    public MultipleSourceInstanceContext getInstanceContext() {
        return instanceContext;
    }

    public Set<Instance> getInstances() {
        return instances;
    }

    private void loadInstances() {
        instances = instanceContext.getInstances();
        log.info("载入 {} 个实例.", instances.size());
    }

    @SuppressWarnings("SameParameterValue")
    private void releaseResource(String resourceName) {
        ClassLoader classLoader = SystemConstants.class.getClassLoader();
        Path path = Paths.get(SystemConstants.USER_DIR + File.separator + resourceName);
        // 检查文件是否已存在
        if (Files.exists(path)) {
            log.debug("文件 {} 已存在, 无需解压.", resourceName);
            return;
        }
        try (InputStream inputStream = classLoader.getResourceAsStream(resourceName)) {
            log.debug("正在解压文件 {} 至路径: {}", resourceName, SystemConstants.USER_DIR);
            // 创建输出流，写入文件到指定目录
            OutputStream outputStream = Files.newOutputStream(path);
            byte[] buffer = new byte[1024];
            int bytesRead;
            if (inputStream != null) {
                while ((bytesRead = inputStream.read(buffer)) != -1)
                    outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.close();
        } catch (Exception e) {
            log.error("文件 {} 解压失败!, 原因: {}", resourceName, e.getMessage());
        }
    }

    private void runInstances() {
        Assert.notNull(scheduledThreadPoolExecutor);
        Assert.notNull(instances);

        for (Instance i : instances) {
            if (!i.validate()) {
                log.warn("实例{}({})参数校验不通过, 将不会被运行.", i.getName(), i.getType());
                continue;
            }
            // 初始化实例
            i.refresh();
            ScheduledFuture<?> future = scheduledThreadPoolExecutor.scheduleWithFixedDelay(i, 0,
                    i.getInterval(), TimeUnit.SECONDS);
            runningInstances.put(i.getName(), future);
            log.info("{}({})已启动, 运行周期 {} 秒.", i.getName(), i.getType(), i.getInterval());
        }
    }

    private void initThreadPool(int coreSize) {
        Assert.isLargerThan(coreSize, 1);
        scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(coreSize, new TaskThreadFactory());

        // 初始化一个线程保活
        scheduledThreadPoolExecutor.submit(() -> {});

        // 提交定时获取网络IP的定时任务
        scheduledThreadPoolExecutor.scheduleAtFixedRate(() -> {
            InstanceContextHolder.setAdditional("ip-update");
            log.info("正在尝试获取本机最新的IP地址.");
            IPAddressResponse response = IPAddressClient.instance.getIPAddress();
            String ip;
            if(response != null
                    && (ip = response.getQuery()) != null) {
                NetworkContextHolder.setIpAddress(ip);
                log.info("本机最新公网IP地址 => {}", ip);
            }
            InstanceContextHolder.clearAdditional();
        }, 0, configuration.getLong(KEY_TASK_REFRESH_INTERVAL_IP, 300L), TimeUnit.SECONDS);

        // 添加进程结束钩子函数
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            InstanceContextHolder.setAdditional("stopping");
            log.info("程序正在关闭中, 可能需要一定时间.");
            scheduledThreadPoolExecutor.shutdown();
            log.info("已关闭.");
            InstanceContextHolder.clearAdditional();
        }, "DDNS-ShutDownHook"));
    }

    private void checkAndCloseSafely() {
        if (scheduledThreadPoolExecutor == null)
            return;

        scheduledThreadPoolExecutor.shutdown();
		try {
			boolean result = scheduledThreadPoolExecutor.awaitTermination(5, TimeUnit.SECONDS);
            if (result) {
                log.error("线程池无法在正常的时间范围内关闭, 将强制关闭线程池!");
                if (!scheduledThreadPoolExecutor.isShutdown())
                    scheduledThreadPoolExecutor.shutdownNow();
            }
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}

        instances.clear();

        runningInstances.clear();
	}
}
