package com.serliunx.ddns.test.support;

import com.serliunx.ddns.support.ipprovider.IcanhazipProvider;
import com.serliunx.ddns.support.ipprovider.IpApiProvider;
import com.serliunx.ddns.support.ipprovider.Provider;
import com.serliunx.ddns.support.ipprovider.ScheduledProvider;
import org.junit.Test;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 供应器测试
 *
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @version 1.0.3
 * @since 2024/11/25
 */
public class ProviderTest {

    @Test
    public void testIpApiProvider() {
        Provider provider = new IpApiProvider();
        System.out.println(provider.get());
    }

    @Test
    public void testScheduledProvider() throws Exception {
        ScheduledProvider provider = new ScheduledProvider(new IpApiProvider(), 3);
        String ip = provider.get();
        System.out.println(ip);
    }

    @Test
    public void testIcanhazipProvider() {
        Provider provider = new IcanhazipProvider();
        System.out.println(provider.get());
    }

    @Test
    public void testScheduledProviderForRunnable() throws InterruptedException {
        ScheduledExecutorService ses = Executors.newScheduledThreadPool(1);
        ScheduledProvider provider = new ScheduledProvider(new IpApiProvider(), 3);
        provider.close();

        ses.scheduleAtFixedRate(provider, 0, 1000, TimeUnit.MILLISECONDS);
        provider.whenUpdate(ip -> System.out.println("ip update: " + ip));

        TimeUnit.SECONDS.sleep(120);
    }
}
