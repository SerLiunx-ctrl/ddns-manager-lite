package com.serliunx.ddns.support.ipprovider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 抽象的ip提供器, 定义公共逻辑
 *
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @version 1.0.3
 * @since 2024/11/25
 */
public abstract class AbstractProvider implements Provider {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    /**
     * 运行期间总共的ip查询次数
     */
    protected long total;
    /**
     * 上次发生的变动的ip地址
     */
    protected String last = null;
    /**
     * 缓存的最新ip地址
     * <li> !!!该地址为上次获得最新地址, 不一定为当前最新的地址
     */
    protected String cache = null;

    @Override
    public long getCount() {
        return total;
    }

    @Override
    public String getLast() {
        return last;
    }

    @Override
    public void init() {
        //do nothing.
    }

    @Override
    public String get() {
        String ipAddress = doGet();
        if (ipAddress == null) {
            log.error("ip地址获取失败!");
            return null;
        }
        total++;

        if (cache == null ||
                !cache.equals(ipAddress)) {
            last = cache;
            cache = ipAddress;
        }

        return ipAddress;
    }

    @Override
    public String getCache() {
        return cache;
    }

    /**
     * 获取具体的ip地址
     */
    protected abstract String doGet();
}
