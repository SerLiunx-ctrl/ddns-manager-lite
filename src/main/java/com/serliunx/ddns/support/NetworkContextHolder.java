package com.serliunx.ddns.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 网络参数上下文, 目前仅用于存储本机网络IP
 *
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @version 1.0.0
 * @since 2024/5/15
 */
public final class NetworkContextHolder {

    private static final Logger log = LoggerFactory.getLogger(NetworkContextHolder.class);
    private static final Lock IP_LOCK = new ReentrantLock();
    // 防止初始化未完成.
    private static final CountDownLatch IP_CONTEXT_WAIT_LATCH = new CountDownLatch(1);
    // 外网IP地址获取
    private static final Integer IP_CONTEXT_TIME_OUT = 5;
    private static volatile String IP_ADDRESS;
    /**
     * 失败计数
     * <p>
     * 失败次数过多后, 会影响
     */
    private static final AtomicInteger FAILED_COUNTS = new AtomicInteger();

    // private-ctor
    private NetworkContextHolder() {throw new UnsupportedOperationException();}

    /**
     * 尝试设置IP地址
     *
     * @param i 新的ip地址, 为空时设置会失败
     */
    public static void setIpAddress(String i) {
        if (i == null
                || i.isEmpty()) {
            log.error("IP 地址不能为空!");
            FAILED_COUNTS.incrementAndGet();
            return;
        }
        try {
            IP_LOCK.lock();
            IP_ADDRESS = i;
            if (IP_CONTEXT_WAIT_LATCH.getCount() > 0) {
                IP_CONTEXT_WAIT_LATCH.countDown();
            }
        } finally {
            FAILED_COUNTS.set(0);
            IP_LOCK.unlock();
        }
    }

    /**
     * 获取所缓存的ip的地址
     * <p>
     * 设置失败次数过多时将忽略已保存的缓存值，防止多次将旧IP重复更新.
     *
     * @return ip地址
     */
    public static String getIpAddress() {
        if (FAILED_COUNTS.get() > 10) {
            log.warn("更新失败次数过多, 不在返回IP地址直到下次成功更新!");
            return null;
        }
        if (IP_ADDRESS != null) {
            return IP_ADDRESS;
        }
        try {
            if (!IP_CONTEXT_WAIT_LATCH.await(IP_CONTEXT_TIME_OUT, TimeUnit.SECONDS)) {
                log.error("IP地址获取超时.");
                return null;
            }
            return IP_ADDRESS;
        } catch (InterruptedException e) {
            log.error("IP地址获取出现异常 => {}", e.getMessage());
        }
        return null;
    }
}
