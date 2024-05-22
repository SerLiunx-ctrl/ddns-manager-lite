package com.serliunx.ddns.core.context;

import com.serliunx.ddns.core.factory.InstanceFactory;

/**
 * @author SerLiunx
 * @since 1.0
 */
public interface InstanceContext extends InstanceFactory {

    @Override
    default int getPriority() {
        return 0;
    }
}
