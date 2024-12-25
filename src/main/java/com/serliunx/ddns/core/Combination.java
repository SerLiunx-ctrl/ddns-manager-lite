package com.serliunx.ddns.core;

import java.util.Collection;

/**
 * 组合, 将多个对象以一定的逻辑相组合
 *
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @version 1.0.3
 * @since 2024/12/24
 *
 * @param <E> 合并的类型
 */
public interface Combination<E> {

    /**
     * 组合指定对象
     *
     * @param e 对象
     */
    void from(E e);

    /**
     * 批量组合对象
     *
     * @param es 对象集合
     */
    void from(Collection<? extends E> es);

    /**
     * 获取原始对象(未组合前的)
     *
     * @return 原始对象
     */
    E getOriginal();

    /**
     * 获取该对象中所有组合的来源
     *
     * @return 所有组合来源
     */
    Collection<? extends E> getCombinations();
}
