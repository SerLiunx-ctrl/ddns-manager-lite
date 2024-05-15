package com.serliunx.ddns.support.feign.client;

import com.serliunx.ddns.support.feign.JacksonDecoder;
import com.serliunx.ddns.support.feign.JacksonEncoder;
import com.serliunx.ddns.support.feign.client.entity.IPAddressResponse;
import feign.Feign;
import feign.Request;
import feign.RequestLine;

import java.util.concurrent.TimeUnit;

/**
 * 本机外网IP地址获取
 * @author SerLiunx
 * @since 1.0
 */
@SuppressWarnings("all")
public interface IPAddressClient {

    static final String url = "http://ip-api.com";

    static final IPAddressClient instance = getInstance();

    /**
     * 获取本机外网IP地址
     * @return IPAddressResponse
     */
    @RequestLine("GET /json")
    IPAddressResponse getIPAddress();

    static IPAddressClient getInstance(){
        return Feign.builder()
                .encoder(JacksonEncoder.getInstance())
                .decoder(JacksonDecoder.getInstance())
                .options(new Request.Options(10,
                        TimeUnit.SECONDS, 10,
                        TimeUnit.SECONDS, true))
                .target(IPAddressClient.class, url);
    }
}
