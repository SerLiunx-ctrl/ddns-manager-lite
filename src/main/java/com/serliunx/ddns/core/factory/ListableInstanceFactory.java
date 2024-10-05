package com.serliunx.ddns.core.factory;

import com.serliunx.ddns.constant.InstanceType;
import com.serliunx.ddns.core.instance.Instance;

import java.util.Map;
import java.util.Set;

/**
 * 扩展型实自工厂接口, 定义了批量获取、筛选实例的方式.
 *
 * @see InstanceFactory
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @version 1.0.0
 * @since 2024/5/15
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
