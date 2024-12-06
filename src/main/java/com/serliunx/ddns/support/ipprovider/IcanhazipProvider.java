package com.serliunx.ddns.support.ipprovider;

import com.serliunx.ddns.support.okhttp.HttpClient;

/**
 * ip数据提供商 <a href="https://icanhazip.com/">icanhazip</a>
 *
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @since 2024/12/6
 */
public final class IcanhazipProvider extends AbstractProvider {

    @Override
    protected String doGet() {
        return HttpClient.httpGet("https://icanhazip.com/");
    }
}
