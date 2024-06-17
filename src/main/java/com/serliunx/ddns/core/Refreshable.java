package com.serliunx.ddns.core;

/**
 * 刷新逻辑
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @version 1.0.0
 * @since 2024/5/15
 */
@FunctionalInterface
public interface Refreshable {

    /**
     * 刷新(初始化)
     */
    void refresh();
}
