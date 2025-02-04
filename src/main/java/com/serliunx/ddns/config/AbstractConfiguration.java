package com.serliunx.ddns.config;

import com.serliunx.ddns.constant.ConfigurationKeys;
import com.serliunx.ddns.support.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
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

    /**
     * 日志
     */
    protected final Logger log = LoggerFactory.getLogger(this.getClass());
    /**
     * 配置值存储
     */
    protected final Map<String, String> valueMap = new LinkedHashMap<>(16);
    /**
     * 上下文更改锁
     */
    protected final Lock contextLock = new ReentrantLock();
    /**
     * 监听器
     * <li> 仅初始化时做增删改操作.
     */
    protected final Map<String, List<ConfigListener>> listeners = new HashMap<>(16);

    /**
     * 监听所有配置键的监听器标识符
     */
    public static final String ALL_KEYS_LISTENERS_TAG = "ALL_KEYS_LISTENERS_TAG";

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
    public boolean modify(String key, Object value) {
        try {
            contextLock.lock();
            if (!valueMap.containsKey(key))
                return false;
            String oldVal = valueMap.get(key);
            String newVal = String.valueOf(value);
            valueMap.put(key, newVal);

			try {
				invokeListeners(key, oldVal, newVal);
			} catch (Exception e) {
				log.warn("监听器执行出现异常 => {}", e.getMessage());
			}

			return true;
        } finally {
            contextLock.unlock();
        }
    }

    @Override
    public void modify(String key, Object value, boolean createIfAbsent) {
        try {
            contextLock.lock();
            boolean invoke = false;
            String oldVal = valueMap.get(key);
            String newVal = String.valueOf(value);
            if (!valueMap.containsKey(key)) {
                if (createIfAbsent) {
                    valueMap.put(key, newVal);
                    invoke = true;
                }
            } else {
                valueMap.put(key, newVal);
                invoke = true;
            }

            if (!invoke)
                return;
            try {
                invokeListeners(key, oldVal, newVal);
            } catch (Exception e) {
                log.warn("监听器执行出现异常[CIA] => {}", e.getMessage());
            }
        } finally {
            contextLock.unlock();
        }
    }

    @Override
    public void addListener(ConfigListener listener) {
        Collection<String> keys = listener.interestedIn();
        Assert.notNull(keys);
        if (keys.isEmpty()) {
            listeners.computeIfAbsent(ALL_KEYS_LISTENERS_TAG, key -> new ArrayList<>())
                    .add(listener);
        } else {
            keys.forEach(k -> {
                listeners.computeIfAbsent(k, k1 -> new ArrayList<>())
                        .add(listener);
            });
        }
    }

    @Override
    public Map<String, List<ConfigListener>> getListeners() {
        return null;
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
            contextLock.lock();
            // 清空原有的配置信息
            valueMap.clear();
            load0();
        }finally {
            contextLock.unlock();
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

    /**
     * 触发监听器
     */
    private void invokeListeners(String key, Object oldVal, Object newVal) throws Exception {
        // 触发监听了所有配置项的监听器
        List<ConfigListener> all = listeners.get(ALL_KEYS_LISTENERS_TAG);
        for (ConfigListener cl : all) {
            cl.onChanged(this, key, oldVal, newVal);
        }
        // 触发其他监听器
        List<ConfigListener> listenerList = listeners.get(key);
        if (listenerList == null || listenerList.isEmpty())
            return;
        for (ConfigListener cl : listenerList) {
            cl.onChanged(this, key, oldVal, newVal);
        }
    }
}
