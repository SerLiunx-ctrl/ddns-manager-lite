package com.serliunx.ddns.core.factory;

import com.serliunx.ddns.core.Clearable;
import com.serliunx.ddns.core.Priority;
import com.serliunx.ddns.core.Refreshable;
import com.serliunx.ddns.core.instance.Instance;

/**
 * 简单的实例工厂逻辑定义, 添加、获取实例.
 * <li> 属于顶层接口, 定义了通用行为: 如刷新和清理逻辑、排序规则等.
 *
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @version 1.0.0
 * @since 2024/5/15
 */
public interface InstanceFactory extends Priority, Comparable<InstanceFactory>, Refreshable, Clearable {

    /**
     * 添加实例
     * <li> 此方法默认为不覆盖的方式添加, 即如果存在则添加失败, 没有任何返回值和异常.
     *
     * @param instance 实例信息
     */
    void addInstance(Instance instance);

    /**
     * 根据实例名称获取实例
     *
     * @param instanceName 实例名称
     * @return 实例信息, 如果不存在则会抛出异常
     */
    Instance getInstance(String instanceName);

    @Override
    default int compareTo(InstanceFactory o) {
        if (getPriority() < o.getPriority())
            return 1;
        else if (this.getPriority() > o.getPriority())
            return -1;
        return 0;
    }
}
