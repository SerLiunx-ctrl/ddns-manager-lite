package com.serliunx.ddns.core.factory;

import com.serliunx.ddns.core.instance.Instance;

/**
 * 可持久化的实例工厂, 支持编辑、保存实例数据.
 *
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @version 1.0.3
 * @since 2024/11/20
 */
public interface PersistentInstanceFactory extends InstanceFactory {

    /**
     * 保存实例信息
     *
     * @param instance 实例
     * @return 成功保存返回真, 否则返回假.
     */
    boolean save(Instance instance);
}
