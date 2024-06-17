package com.serliunx.ddns.core.factory;

import com.serliunx.ddns.core.instance.Instance;

import java.util.Collections;
import java.util.Set;

/**
 * 数据库实例工厂
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @version 1.0.0
 * @since 2024/5/15
 */
public abstract class DatabaseInstanceFactory extends AbstractInstanceFactory {

    @Override
    protected Set<Instance> load() {
        return Collections.emptySet();
    }
}
