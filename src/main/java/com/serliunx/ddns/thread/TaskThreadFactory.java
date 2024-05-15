package com.serliunx.ddns.thread;

import com.serliunx.ddns.support.Assert;
import com.serliunx.ddns.support.SystemSupport;
import org.jetbrains.annotations.NotNull;
import org.slf4j.MDC;

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
        Runnable runnable = () -> {
            MDC.put("pid", SystemSupport.getPid());
            r.run();
        };
        return new Thread(runnable, String.format("ddns-task-%s", count.getAndIncrement()));
    }
}
