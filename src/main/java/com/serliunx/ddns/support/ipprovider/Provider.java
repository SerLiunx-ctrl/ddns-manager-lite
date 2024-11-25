package com.serliunx.ddns.support.ipprovider;

/**
 * ip供应器接口定义
 *
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @version 1.0.3
 * @since 2024/11/25
 */
public interface Provider {

    /**
     * 获取本次运行期间ip的查询次数
     *
     * @return 查询次数
     */
    long getCount();

    /**
     * 获取上次发生变动的ip地址
     *
     * @return 上次发生变动的ip地址
     */
    String getLast();

    /**
     * 获取最新的ip
     *
     * @return 最新的ip
     */
    String get();

    /**
     * 获取缓存的最新ip地址
     * <li> !!!该地址为上次获得最新地址, 不一定为当前最新的地址
     *
     * @return 缓存的最新ip地址
     */
    String getCache();

    /**
     * 初始化
     */
    void init();

}
