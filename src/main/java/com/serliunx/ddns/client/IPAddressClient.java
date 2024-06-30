package com.serliunx.ddns.client;

import com.serliunx.ddns.client.entity.IPAddressResponse;
import com.serliunx.ddns.support.feign.JacksonDecoder;
import com.serliunx.ddns.support.feign.JacksonEncoder;
import feign.Feign;
import feign.Logger;
import feign.RequestLine;
import feign.Retryer;
import feign.slf4j.Slf4jLogger;

/**
 * 本机外网IP地址获取
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @version 1.0.0
 * @since 2024/5/15
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

    static IPAddressClient getInstance() {
        return Feign.builder()
                .logger(new Slf4jLogger())
                .logLevel(Logger.Level.BASIC)
                .retryer(Retryer.NEVER_RETRY)
                .encoder(JacksonEncoder.getInstance())
                .decoder(JacksonDecoder.getInstance())
                .target(IPAddressClient.class, url);
    }
}