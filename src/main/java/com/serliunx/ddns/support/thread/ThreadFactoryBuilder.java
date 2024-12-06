package com.serliunx.ddns.support.thread;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 线程工厂构建
 *
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @version 1.0.3
 * @since 2024/12/3
 */
public final class ThreadFactoryBuilder {

    /**
     * 实例
     */
    private static final ThreadFactoryBuilder INSTANCE = new ThreadFactoryBuilder();

    private ThreadFactoryBuilder() {}

    /**
     * 获取实例
     */
    public static ThreadFactoryBuilder builder() {
        return INSTANCE;
    }

    /**
     * 线程工厂之模板名称
     *
     * @param pattern 名称模板(如: task-util-%s), %s将根据数量递增
     * @return 线程工厂
     */
    public ThreadFactory ofNamePattern(final String pattern) {
        return new NamePatternThreadFactory(pattern);
    }

    /**
     * 线程工厂之模板名称
     */
    private static class NamePatternThreadFactory implements ThreadFactory {

        private final AtomicInteger counter = new AtomicInteger(0);

        private final String pattern;

        public NamePatternThreadFactory(String pattern) {
            this.pattern = pattern;
        }

        @Override
        public Thread newThread(@NotNull Runnable r) {
            return new Thread(r, String.format(pattern, counter.getAndIncrement()));
        }
    }
}
