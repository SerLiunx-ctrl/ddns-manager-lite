package com.serliunx.ddns.support;

import com.serliunx.ddns.config.Configuration;
import com.serliunx.ddns.core.context.MultipleSourceInstanceContext;

/**
 * @author SerLiunx
 * @since 1.0
 */
public final class Configurer {

    private Configuration configuration;
    private MultipleSourceInstanceContext instanceContext;

    Configurer(){}

    public Configurer configuration(Configuration configuration){
        Assert.notNull(configuration);
        this.configuration = configuration;
        return this;
    }

    public Configurer instanceContext(MultipleSourceInstanceContext instanceContext){
        Assert.notNull(instanceContext);
        this.instanceContext = instanceContext;
        return this;
    }

    public SystemInitializer done(){
        Assert.notNull(configuration, instanceContext);
        return new SystemInitializer(configuration, instanceContext);
    }
}
