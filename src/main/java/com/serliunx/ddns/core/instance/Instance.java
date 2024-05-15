package com.serliunx.ddns.core.instance;

import com.serliunx.ddns.constant.InstanceSource;
import com.serliunx.ddns.constant.InstanceType;
import com.serliunx.ddns.support.Refreshable;

/**
 * @author SerLiunx
 * @since 1.0
 */
public interface Instance extends Runnable, Refreshable {

    /**
     * 获取实例名称
     * @return 实例名称
     */
    String getName();

    /**
     * 设置实例名称
     * @param name 实例名称
     */
    void setName(String name);

    /**
     * 获取父实例名称
     * @return 父实例名称
     */
    String getFatherName();

    /**
     * 设置父实例名称
     * @param fatherName 父实例名称
     */
    void setFatherName(String fatherName);

    /**
     * 获取实例执行周期 (单位秒)
     * @return 执行周期
     */
    Long getInterval();

    /**
     * 设置实例执行周期 (单位秒)
     * @param interval 执行周期
     */
    void setInterval(Long interval);

    /**
     * 获取实例类型
     * @return 实例类型
     */
    InstanceType getType();

    /**
     * 设置实例类型
     * @param instanceType 实例类型
     */
    void setType(InstanceType instanceType);

    /**
     * 获取实例来源
     * @return 实例来源
     */
    InstanceSource getSource();

    /**
     * 设置实例来源
     * @param instanceSource 实例来源
     */
    void setSource(InstanceSource instanceSource);

    /**
     * 实例参数校验
     * @return 通过校验返回真, 否则返回假
     */
    boolean validate();
}
