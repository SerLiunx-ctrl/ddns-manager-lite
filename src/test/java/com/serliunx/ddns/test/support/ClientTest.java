package com.serliunx.ddns.test.support;

import com.serliunx.ddns.support.feign.client.IPAddressClient;
import org.junit.Test;

/**
 * @author SerLiunx
 * @since 1.0
 */
public class ClientTest {

    @Test
    public void test(){
        IPAddressClient client = IPAddressClient.instance;
        System.out.println(client.getIPAddress().getQuery());
    }
}
