package com.serliunx.ddns.thread;

import com.serliunx.ddns.support.Assert;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 简易的实例活动相关的线程工厂, 仅仅定义了线程的名称规则.
 *
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @version 1.0.0
 * @since 2024/5/15
 */
public class TaskThreadFactory implements ThreadFactory {

    private final AtomicInteger count = new AtomicInteger(0);

    @Override
    public Thread newThread(@NotNull Runnable r) {
        Assert.notNull(r);
        return new Thread(r, String.format(getNamePattern(), count.getAndIncrement()));
    }

    protected String getNamePattern() {
        return "ddns-task-%s";
    }
}
