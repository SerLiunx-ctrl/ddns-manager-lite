package com.serliunx.ddns.util;

import com.serliunx.ddns.core.instance.Instance;
import com.serliunx.ddns.support.Assert;

/**
 * 实例相关工具方法集合
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @version 1.0.0
 * @since 2024/5/15
 */
public final class InstanceUtils {

    private InstanceUtils() {throw new UnsupportedOperationException();}

    public static void validateInstance(Instance instance) {
        Assert.notNull(instance);
        String instanceName = instance.getName();
        if (instanceName == null || instanceName.isEmpty()) {
            throw new NullPointerException();
        }
    }
}
