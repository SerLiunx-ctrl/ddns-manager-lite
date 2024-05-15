package com.serliunx.ddns.core.context;

import com.serliunx.ddns.core.factory.InstanceFactory;
import com.serliunx.ddns.support.Refreshable;

/**
 * @author SerLiunx
 * @since 1.0
 */
public interface InstanceContext extends InstanceFactory, Refreshable {

    @Override
    default int getPriority() {
        return 0;
    }
}
