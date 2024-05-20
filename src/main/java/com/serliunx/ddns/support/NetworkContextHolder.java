package com.serliunx.ddns.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 网络参数上下文, 目前仅用于存储本机网络IP
 * @author SerLiunx
 * @since 1.0
 */
public final class NetworkContextHolder {

    private static final Logger log = LoggerFactory.getLogger(NetworkContextHolder.class);
    private static final Lock IP_LOCK = new ReentrantLock();
    // 防止初始化未完成.
    private static final CountDownLatch IP_CONTEXT_WAIT_LATCH = new CountDownLatch(1);
    // 外网IP地址获取
    private static final Integer IP_CONTEXT_TIME_OUT = 5;
    private static volatile String IP_ADDRESS;

    private NetworkContextHolder(){throw new UnsupportedOperationException();}

    public static void setIpAddress(String i) {
        try {
            IP_LOCK.lock();
            IP_ADDRESS = i;
            if(IP_CONTEXT_WAIT_LATCH.getCount() > 0){
                IP_CONTEXT_WAIT_LATCH.countDown();
            }
        }finally {
            IP_LOCK.unlock();
        }
    }

    public static String getIpAddress() {
        if(IP_ADDRESS != null)
            return IP_ADDRESS;
        try {
            if(!IP_CONTEXT_WAIT_LATCH.await(IP_CONTEXT_TIME_OUT, TimeUnit.SECONDS)) {
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
