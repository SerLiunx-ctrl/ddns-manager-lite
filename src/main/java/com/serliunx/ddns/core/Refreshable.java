package com.serliunx.ddns.core;

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
}
