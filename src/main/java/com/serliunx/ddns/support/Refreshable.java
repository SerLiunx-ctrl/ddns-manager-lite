package com.serliunx.ddns.support;

/**
 * 刷新逻辑
 * @author SerLiunx
 * @since 1.0
 */
@FunctionalInterface
public interface Refreshable {

    /**
     * 刷新(初始化)
     */
    void refresh();

    /**
     * 刷新后逻辑定义, 一般用于资源清理
     */
    default void afterRefresh(){}
}
