package com.serliunx.ddns.config;

import com.serliunx.ddns.core.Priority;
import com.serliunx.ddns.core.Refreshable;

import java.util.Map;

/**
 * 配置信息逻辑定义
 *
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @version 1.0.0
 * @since 2024/5/15
 */
public interface Configuration extends Refreshable, Priority {

    /**
     * 获取整数
     * @param key 键
     * @return 整数
     */
    Integer getInteger(String key);

    /**
     * 获取整数, 带默认值
     * @param key 键
     * @param defaultValue 默认值
     * @return 整数
     */
    Integer getInteger(String key, Integer defaultValue);

    /**
     * 获取长整数
     * @param key 键
     * @return 长整数
     */
    Long getLong(String key);

    /**
     * 获取长整数
     * @param key 键
     * @param defaultValue 默认值
     * @return 长整数
     */
    Long getLong(String key, Long defaultValue);

    /**
     * 获取字符串
     * @param key 键
     * @return 字符串
     */
    String getString(String key);

    /**
     * 获取字符串
     * @param key 键
     * @param defaultValue 默认值
     * @return 字符串
     */
    String getString(String key, String defaultValue);

    /**
     * 获取布尔值
     * @param key 键
     * @return 布尔值
     */
    Boolean getBoolean(String key);

    /**
     * 获取布尔值
     * @param key 键
     * @param defaultValue 默认值
     * @return 布尔值
     */
    Boolean getBoolean(String key, Boolean defaultValue);

    /**
     * 获取枚举值
     * @param clazz 枚举类
     * @param key 键
     * @return 枚举值
     * @param <T> 枚举类型参数
     */
    @SuppressWarnings("rawtypes")
    <T extends Enum> T getEnum(Class<T> clazz, String key);

    /**
     * 获取枚举值
     * @param clazz 枚举类
     * @param key 键
     * @param defaultValue 默认值
     * @return 枚举值
     * @param <T> 枚举类型参数
     */
    @SuppressWarnings("rawtypes")
    <T extends Enum> T getEnum(Class<T> clazz, String key, T defaultValue);

    /**
     * 获取所有的键值对
     * @return 配置文件所有成功加载的键值对
     */
    Map<String, String> getAllKeyAndValue();
}
