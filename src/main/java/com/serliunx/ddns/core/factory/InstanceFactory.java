package com.serliunx.ddns.core.factory;

import com.serliunx.ddns.core.Priority;
import com.serliunx.ddns.core.instance.Instance;
import com.serliunx.ddns.support.Refreshable;

/**
 * @author SerLiunx
 * @since 1.0
 */
public interface InstanceFactory extends Priority, Comparable<InstanceFactory>, Refreshable {

    /**
     * 添加实例
     * <li> 此方法默认为不覆盖的方式添加, 即如果存在则添加失败, 没有任何返回值和异常.
     * @param instance 实例信息
     */
    void addInstance(Instance instance);

    /**
     * 添加实例
     * @param instance 实例信息
     * @param override 是否覆盖原有的同名实例
     * @return 成功添加返回真, 否则返回假
     */
    boolean addInstance(Instance instance, boolean override);

    /**
     * 根据实例名称获取实例
     * @param instanceName 实例名称
     * @return 实例信息, 如果不存在则会抛出异常
     */
    Instance getInstance(String instanceName);

    @Override
    default int compareTo(InstanceFactory o) {
        if(getPriority() < o.getPriority()){
            return 1;
        } else if (this.getPriority() > o.getPriority()) {
            return -1;
        }
        return 0;
    }
}
