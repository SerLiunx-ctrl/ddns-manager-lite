package com.serliunx.ddns.thread;

import com.serliunx.ddns.support.Assert;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author SerLiunx
 * @since 1.0
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
