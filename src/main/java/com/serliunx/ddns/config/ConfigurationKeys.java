package com.serliunx.ddns.config;

/**
 * 配置文件键常量信息
 *
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @version 1.0.0
 * @since 2024/5/15
 */
public final class ConfigurationKeys {

    private ConfigurationKeys() {throw new UnsupportedOperationException();}

    /**
     * 线程池核心线程数量
     */
    public static final String KEY_THREAD_POOL_CORE_SIZE = "system.pool.core.size";

    /**
     * 启动时是否输出配置信息
     */
    public static final String KEY_CFG_LOG_ONSTART = "system.cfg.log.onstart";

    /**
     * 定时任务周期: 获取最新IP
     */
    public static final String KEY_TASK_REFRESH_INTERVAL_IP = "system.task.refresh.interval.ip";

    /**
     * 阿里云解析线路
     */
    public static final String KEY_ALIYUN_ENDPOINT = "instance.aliyun.endpoint.url";

    /**
     * http请求超时时间(秒)
     */
    public static final String KEY_HTTP_OVERTIME = "system.http.overtime";
}
