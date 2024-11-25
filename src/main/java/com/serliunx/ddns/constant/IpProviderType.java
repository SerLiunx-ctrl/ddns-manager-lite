package com.serliunx.ddns.constant;

import com.serliunx.ddns.support.ipprovider.IpApiProvider;
import com.serliunx.ddns.support.ipprovider.Provider;

/**
 * ip供应器类型
 *
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @version 1.0.3
 * @since 2024/11/25
 */
public enum IpProviderType {

    /**
     * ip数据提供商 <a href="https://ip-api.com/">ip-api</a>
     * <li> 国外的数据, 国内访问不稳定.
     */
    IP_API(new IpApiProvider()),

    ;

    private final Provider provider;

    IpProviderType(Provider provider) {
        this.provider = provider;
    }

    public Provider getProvider() {
        return provider;
    }
}
