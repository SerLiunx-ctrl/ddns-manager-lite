package com.serliunx.ddns.core.factory;

import com.serliunx.ddns.constant.InstanceType;
import com.serliunx.ddns.core.instance.Instance;

import java.util.Map;
import java.util.Set;

/**
 * @author SerLiunx
 * @since 1.0
 */
public interface ListableInstanceFactory extends InstanceFactory {

    /**
     * 获取所有已加载的实例信息
     * @return 所有实例信息
     */
    Set<Instance> getInstances();

    /**
     * 获取指定类型的实例
     * @param type 类型
     * @return 实例名称-实例信息 键值对.
     */
    Map<String, Instance> getInstanceOfType(InstanceType type);

    /**
     * 获取指定类型的实例
     * @param type 类型名称
     * @return 实例名称-实例信息 键值对.
     */
    default Map<String, Instance> getInstanceOfType(String type) {
        return getInstanceOfType(InstanceType.valueOf(type));
    }
}
