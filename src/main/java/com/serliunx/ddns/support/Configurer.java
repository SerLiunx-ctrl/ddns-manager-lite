package com.serliunx.ddns.support;

import com.serliunx.ddns.config.Configuration;
import com.serliunx.ddns.core.context.MultipleSourceInstanceContext;

/**
 * 系统启动配置器
 *
 * @see SystemInitializer
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @version 1.0.0
 * @since 2024/5/15
 */
public final class Configurer {

    private Configuration configuration;
    private MultipleSourceInstanceContext instanceContext;
    private boolean clearCache;

    Configurer(){}

    public Configurer configuration(Configuration configuration) {
        Assert.notNull(configuration);
        this.configuration = configuration;
        return this;
    }

    public Configurer instanceContext(MultipleSourceInstanceContext instanceContext) {
        Assert.notNull(instanceContext);
        this.instanceContext = instanceContext;
        return this;
    }

    public Configurer clearCache(boolean clearCache) {
        this.clearCache = clearCache;
        return this;
    }

    public SystemInitializer done() {
        Assert.notNull(configuration, instanceContext);
        return new SystemInitializer(configuration, instanceContext, clearCache);
    }
}
