package com.serliunx.ddns.core;

/**
 * 定义一个对象的优先级
 * <li> 数字越大, 优先级越小
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @version 1.0.0
 * @since 2024/5/15
 */
@FunctionalInterface
public interface Priority {

    /**
     * 获取该对象的优先级
     * <li> 数字越大, 优先级越小
     * @return 优先级
     */
    int getPriority();
}
