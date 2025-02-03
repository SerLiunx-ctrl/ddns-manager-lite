package com.serliunx.ddns.config;

import java.util.Collection;
import java.util.Collections;

/**
 * 配置监听器
 * <li> 针对配置的变动所需要执行的逻辑
 *
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @version 1.0.4
 * @since 2025/2/3
 */
@FunctionalInterface
public interface ConfigListener {

	/**
	 * 指定当前监听器所感兴趣的配置项(可多个)
	 * <li> 为空时即监听所有配置项
	 *
	 * @return 感兴趣的配置项
	 */
	default Collection<String> interestedIn() {
		return Collections.emptyList();
	}

	/**
	 * 配置项发生了变动的回调
	 *
	 * @param configuration	配置
	 * @param key			配置键
	 * @param oldVal		旧值
	 * @param newVal		新值
	 */
	void onChanged(Configuration configuration, String key, Object oldVal, Object newVal) throws Exception;
}
