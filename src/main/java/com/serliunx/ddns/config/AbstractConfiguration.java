package com.serliunx.ddns.config;

import com.serliunx.ddns.support.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 配置信息的抽象实现, 定义公共逻辑
 *
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @version 1.0.0
 * @since 2024/5/15
 */
public abstract class AbstractConfiguration implements Configuration {

    protected final Logger log = LoggerFactory.getLogger(this.getClass());
    protected final Map<String, String> valueMap = new LinkedHashMap<>(16);
    private final Lock loadLock = new ReentrantLock();

    public AbstractConfiguration() {}

    @Override
    public Integer getInteger(String key) {
        Assert.notNull(key);
        String v = valueMap.get(key);
        return v == null ? null : Integer.valueOf(v);
    }

    @Override
    public Integer getInteger(String key, Integer defaultValue) {
        Assert.notNull(key, defaultValue);
        Integer v = getInteger(key);
        return v == null ? defaultValue : v;
    }

    @Override
    public Long getLong(String key) {
        Assert.notNull(key);
        String v = valueMap.get(key);
        return v == null ? null : Long.valueOf(v);
    }

    @Override
    public Long getLong(String key, Long defaultValue) {
        Assert.notNull(key, defaultValue);
        Long v = getLong(key);
        return v == null ? defaultValue : v;
    }

    @Override
    public String getString(String key) {
        Assert.notNull(key);
        return valueMap.get(key);
    }

    @Override
    public String getString(String key, String defaultValue) {
        Assert.notNull(key, defaultValue);
        String v = getString(key);
        return v == null ? defaultValue : v;
    }

    @Override
    public Boolean getBoolean(String key) {
        Assert.notNull(key);
        return Boolean.valueOf(valueMap.get(key));
    }

    @Override
    public Boolean getBoolean(String key, Boolean defaultValue) {
        Assert.notNull(key, defaultValue);
        String value = valueMap.get(key);
        return value == null ? defaultValue : Boolean.valueOf(value);
    }

    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    public <T extends Enum> T getEnum(Class<T> clazz, String key) {
        String rawValue = getString(key);
        Assert.notNull(rawValue);
        return (T)Enum.valueOf(clazz, rawValue);
    }

    @Override
    @SuppressWarnings({"rawtypes"})
    public <T extends Enum> T getEnum(Class<T> clazz, String key, T defaultValue) {
        T value = null;
        try {
            value = getEnum(clazz, key);
        }catch (Exception ignored){}
        return value == null ? defaultValue : value;
    }

    @Override
    public void refresh() {
        // 刷新配置信息
        refresh0();
        final Boolean needPrint = getBoolean(ConfigurationKeys.KEY_CFG_LOG_ONSTART, false);
        if (needPrint)
            printDetails();
    }

    @Override
    public Map<String, String> getAllKeyAndValue() {
        return valueMap;
    }

    @Override
    public int getPriority() {
        return Integer.MAX_VALUE;
    }

    /**
     * 载入配置信息请加锁
     */
    protected void load() {
        try {
            loadLock.lock();
            // 清空原有的配置信息
            valueMap.clear();
            load0();
        }finally {
            loadLock.unlock();
        }
    }

    /**
     * 打印配置信息
     */
    protected void printDetails(){
        log.info("=====配置信息=====");
        valueMap.forEach((k, v) -> {
            log.info("{} = {}", k, v);
        });
        log.info("=================");
    }

    /**
     * 具体的刷新逻辑
     */
    protected abstract void refresh0();

    /**
     * 载入逻辑
     */
    protected abstract void load0();
}
