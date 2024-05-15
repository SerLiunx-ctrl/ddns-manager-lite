package com.serliunx.ddns.core.factory;

import com.serliunx.ddns.core.instance.Instance;

import java.util.Collections;
import java.util.Set;

/**
 * 数据库实例工厂
 * @author SerLiunx
 * @since 1.0
 */
public abstract class DatabaseInstanceFactory extends AbstractInstanceFactory{

    @Override
    protected Set<Instance> load() {
        return Collections.emptySet();
    }
}
