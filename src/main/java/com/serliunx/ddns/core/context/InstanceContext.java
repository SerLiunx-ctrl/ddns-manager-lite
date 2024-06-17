package com.serliunx.ddns.core.context;

import com.serliunx.ddns.core.factory.InstanceFactory;

/**
 * 实例容器接口定义
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @version 1.0.0
 * @since 2024/5/15
 */
public interface InstanceContext extends InstanceFactory {

    @Override
    default int getPriority() {
        return 0;
    }
}
