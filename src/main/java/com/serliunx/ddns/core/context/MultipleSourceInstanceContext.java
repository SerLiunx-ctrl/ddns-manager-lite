package com.serliunx.ddns.core.context;

import com.serliunx.ddns.core.factory.InstanceFactory;
import com.serliunx.ddns.core.factory.ListableInstanceFactory;

import java.util.Set;

/**
 * 多数据源的实例容器, 将多种实例来源汇聚到一起
 * @see InstanceFactory
 * @see InstanceContext
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @version 1.0.0
 * @since 2024/5/15
 */
public interface MultipleSourceInstanceContext extends InstanceContext, ListableInstanceFactory {

    /**
     * 添加一个实例工厂
     * @param listableInstanceFactory 实例工厂
     */
    void addListableInstanceFactory(ListableInstanceFactory listableInstanceFactory);

    /**
     * 获取所有实例工厂
     * @return 实例工厂列表
     */
    Set<ListableInstanceFactory> getListableInstanceFactories();
}
