package com.serliunx.ddns.constant;

import com.serliunx.ddns.core.instance.AliyunInstance;
import com.serliunx.ddns.core.instance.Instance;
import com.serliunx.ddns.core.instance.TencentInstance;

import java.util.HashMap;
import java.util.Map;

/**
 * 实例类型集合
 * @author SerLiunx
 * @since 1.0
 */
public final class InstanceClasses {
    private InstanceClasses(){throw new UnsupportedOperationException();}

    private static final Map<InstanceType, Class<? extends Instance>> instanceTypeMap =
            new HashMap<InstanceType, Class<? extends Instance>>() {
                {
                    put(InstanceType.ALI_YUN, AliyunInstance.class);
                    put(InstanceType.TENCENT_CLOUD, TencentInstance.class);
                }
            };

    public static Class<? extends Instance> match(InstanceType type) {
        return instanceTypeMap.get(type);
    }

    public static Class<? extends Instance> match(String type) {
        return instanceTypeMap.get(InstanceType.valueOf(type));
    }
}
