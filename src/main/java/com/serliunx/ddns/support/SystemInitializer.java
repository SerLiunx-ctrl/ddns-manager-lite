package com.serliunx.ddns.support;

import com.serliunx.ddns.config.Configuration;
import com.serliunx.ddns.constant.ConfigurationKeys;
import com.serliunx.ddns.constant.IpProviderType;
import com.serliunx.ddns.constant.SystemConstants;
import com.serliunx.ddns.core.Clearable;
import com.serliunx.ddns.core.Refreshable;
import com.serliunx.ddns.core.context.MultipleSourceInstanceContext;
import com.serliunx.ddns.core.instance.Instance;
import com.serliunx.ddns.support.ipprovider.Provider;
import com.serliunx.ddns.support.ipprovider.ScheduledProvider;
import com.serliunx.ddns.support.thread.ThreadFactoryBuilder;
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

import static com.serliunx.ddns.constant.ConfigurationKeys.KEY_TASK_REFRESH_INTERVAL_IP;
import static com.serliunx.ddns.constant.ConfigurationKeys.KEY_THREAD_POOL_CORE_SIZE;

/**
 * 系统初始化
 *
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @version 1.0.0
 * @since 2024/5/15
 */
public final class SystemInitializer implements Refreshable, Clearable {

    private static final Logger log = LoggerFactory.getLogger(SystemInitializer.class);

    private final Configuration configuration;
    private final MultipleSourceInstanceContext instanceContext;
    private final boolean clearCache;

    private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;
    private Set<Instance> instances;
    private final Map<String, ScheduledFuture<?>> runningInstances = new HashMap<>(64);
    private ScheduledProvider scheduledProvider;

    SystemInitializer(Configuration configuration, MultipleSourceInstanceContext instanceContext, boolean clearCache) {
        this.configuration = configuration;
        this.instanceContext = instanceContext;
        this.clearCache = clearCache;
    }

    SystemInitializer(Configuration configuration, MultipleSourceInstanceContext instanceContext) {
        this(configuration, instanceContext, true);
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
        ConfigurationContextHolder.setConfiguration(configuration);

        // 获取核心线程数量, 默认为CPU核心数量
        int coreSize = configuration.getInteger(KEY_THREAD_POOL_CORE_SIZE, Runtime.getRuntime().availableProcessors());

        // 初始化ip地址更新任务
        initIpTask();

        // 初始化线程池
        initThreadPool(coreSize);

        // 加载实例(不同的容器加载时机不同)
        loadInstances();

        // 运行实例
        runInstances();

        // 清理实例、配置缓存, 正常情况下读取一次就不需要了
        if (clearCache) {
            clear();
        }
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

    /**
     * 加载实例(不同的容器加载时机不同)
     */
    private void loadInstances() {
        instances = instanceContext.getInstances();
        log.info("载入 {} 个实例.", instances.size());
    }

    /**
     * 资源释放
     */
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
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }
            outputStream.close();
        } catch (Exception e) {
            log.error("文件 {} 解压失败!, 原因: {}", resourceName, e.getMessage());
        }
    }

    /**
     * 运行实例
     */
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
            ScheduledFuture<?> future = scheduledThreadPoolExecutor.scheduleWithFixedDelay(i, 5,
                    i.getInterval(), TimeUnit.SECONDS);
            runningInstances.put(i.getName(), future);
            log.info("{}({})已启动, 运行周期 {} 秒.", i.getName(), i.getType(), i.getInterval());
        }
    }

    /**
     * 初始化线程池
     *
     * @param coreSize 线程池核心线程数量
     */
    private void initThreadPool(int coreSize) {
        Assert.isLargerThan(coreSize, 1);
        scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(coreSize, ThreadFactoryBuilder.builder()
                .ofNamePattern("ddns-task-%s"));

        // 初始化一个线程保活
        scheduledThreadPoolExecutor.submit(() -> {});

        // 添加进程结束钩子函数
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            InstanceContextHolder.setAdditional("stopping");
            log.info("程序正在关闭中, 可能需要一定时间.");
            scheduledThreadPoolExecutor.shutdown();
            log.info("已关闭.");
            InstanceContextHolder.clearAdditional();
        }, "DDNS-ShutDownHook"));
    }

    /**
     * 初始化定时获取IP地址的任务
     */
    private void initIpTask() {
        scheduledProvider = new ScheduledProvider(getInternalProvider(),
                configuration.getLong(KEY_TASK_REFRESH_INTERVAL_IP, 300L));

        scheduledProvider.whenUpdate(ip -> {
            NetworkContextHolder.setIpAddress(ip);
            log.info("本机最新公网IP地址 => {}", ip);
        });
    }

    /**
     * 获取内置的IP供应器（获取IP地址的方式）
     * <p>
     * 根据配置文件中的定义{@link ConfigurationKeys#KEY_IP_PROVIDER_TYPE}, 默认为{@link com.serliunx.ddns.support.ipprovider.IpApiProvider}
     */
    private Provider getInternalProvider() {
        return configuration.getEnum(IpProviderType.class, ConfigurationKeys.KEY_IP_PROVIDER_TYPE,
                IpProviderType.IP_API).getProvider();
    }

    /**
     * 关闭线程池逻辑
     */
    private void checkAndCloseSafely() {
        if (scheduledThreadPoolExecutor == null)
            return;

        scheduledThreadPoolExecutor.shutdown();
		try {
			boolean result = scheduledThreadPoolExecutor.awaitTermination(5, TimeUnit.SECONDS);
            if (!result) {
                log.error("线程池无法在正常的时间范围内关闭, 将强制关闭线程池!");
                if (!scheduledThreadPoolExecutor.isShutdown()) {
                    scheduledThreadPoolExecutor.shutdownNow();
                }
            }
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		} finally {
            instances.clear();
            runningInstances.clear();
        }
	}
}
