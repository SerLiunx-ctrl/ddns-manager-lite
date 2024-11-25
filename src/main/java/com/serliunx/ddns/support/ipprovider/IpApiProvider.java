package com.serliunx.ddns.support.ipprovider;

import com.serliunx.ddns.support.okhttp.HttpClient;
import com.serliunx.ddns.support.okhttp.IPAddressResponse;

/**
 * ip数据提供商 <a href="https://ip-api.com/">ip-api</a>
 * <li> 国外的数据, 国内访问不稳定.
 *
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @version 1.0.3
 * @since 2024/11/25
 */
public final class IpApiProvider extends AbstractProvider {

    @Override
    protected String doGet() {
        IPAddressResponse response = HttpClient.getIPAddress();
        if (response == null) {
            return null;
        }
        return response.getQuery();
    }
}
