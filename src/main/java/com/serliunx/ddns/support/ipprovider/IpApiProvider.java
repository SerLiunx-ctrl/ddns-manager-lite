package com.serliunx.ddns.support.ipprovider;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
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

    private static final ObjectMapper JSON_MAPPER = new JsonMapper();

    @Override
    protected String doGet() {
        final String response = HttpClient.httpGet("http://ip-api.com/json");
        if (response == null
                || response.isEmpty()) {
            return null;
        }

        try {
            IPAddressResponse ipAddressResponse = JSON_MAPPER.readValue(response, IPAddressResponse.class);

            return ipAddressResponse.getQuery();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
