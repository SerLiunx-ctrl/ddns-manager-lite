package com.serliunx.ddns.support.ipprovider;

import com.serliunx.ddns.support.Assert;
import com.serliunx.ddns.support.InstanceContextHolder;
import com.serliunx.ddns.support.thread.ThreadFactoryBuilder;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * 自动更新的ip供应器
 * <li> 异步更新ip, 获取到的ip地址不一定为最新可用的。
 *
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @version 1.0.3
 * @since 2024/11/25
 */
public class ScheduledProvider extends AbstractProvider {

    private final Provider internalProvider;

    /**
     * 执行周期(秒)
     */
    private volatile long timePeriod;
    /**
     * 任务
     */
    private volatile ScheduledFuture<?> task;

    /**
     * 内置线程池
     */
    private ScheduledThreadPoolExecutor poolExecutor = null;
    /**
     * 处理器
     */
    private Consumer<String> valueConsumer = null;
    /**
     * 内置缓存
     */
    private volatile String internalCache = null;

    public ScheduledProvider(Provider internalProvider, long timePeriod) {
        Assert.notNull(internalProvider);
        Assert.isLargerThan(timePeriod, 0);
        this.internalProvider = internalProvider;
        this.timePeriod = timePeriod;
        init();
    }

    public ScheduledProvider(Provider internalProvider) {
        this(internalProvider, 60);
    }

    @Override
    public String get() {
        return internalCache;
    }

    @Override
    public void init() {
        poolExecutor = new ScheduledThreadPoolExecutor(2, ThreadFactoryBuilder.builder()
                .ofNamePattern("ip-provider-%s")
        );
        // 提交
        submitTask();
    }

    /**
     * 更新执行周期
     * <li> 回替换掉现有的更新任务
     *
     * @param timePeriod 新的执行周期
     */
    public void changeTimePeriod(long timePeriod) {
        Assert.isLargerThan(timePeriod, 0);
        this.timePeriod = timePeriod;
        // 取消现有的任务
        task.cancel(true);
        submitTask();
    }

    /**
     * ip更新时需要执行的逻辑
     *
     * @param valueConsumer 逻辑
     */
    public void whenUpdate(Consumer<String> valueConsumer) {
        this.valueConsumer = valueConsumer;
    }

    @Override
    protected String doGet() {
        // 不应该执行到这里
        throw new UnsupportedOperationException();
    }

    /**
     * 提交任务逻辑
     */
    private void submitTask() {
        task = poolExecutor.scheduleAtFixedRate(() -> {
            // 打断时, 终止已有的任务. (逻辑上不应该发生)
            if (Thread.currentThread().isInterrupted()) {
                log.debug("上一个ip更新任务已终止.");
                return;
            }
            InstanceContextHolder.setAdditional("ip-update");
            internalCache = internalProvider.get().trim();

            if (valueConsumer != null) {
                valueConsumer.accept(internalCache);
            }
        }, 0, timePeriod, TimeUnit.SECONDS);
    }
}
