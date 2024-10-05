package com.serliunx.ddns.support;

import com.serliunx.ddns.config.Configuration;

/**
 * 配置信息上下文
 * <li> {@link Configuration}
 *
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @version 1.0.1
 * @since 2024/7/8
 */
public final class ConfigurationContextHolder {

	private static final ThreadLocal<Configuration> CONFIGURATION_HOLDER = new ThreadLocal<>();

	private ConfigurationContextHolder() {throw new UnsupportedOperationException();}

	/**
	 * 获取配置信息
	 *
	 * @return 配置信息
	 */
	public static Configuration getConfiguration() {
		return CONFIGURATION_HOLDER.get();
	}

	/**
	 * 设置配置信息
	 * <li> 一般在程序初始化时调用
	 *
	 * @param configuration 配置信息
	 */
	public static void setConfiguration(Configuration configuration) {
		CONFIGURATION_HOLDER.set(configuration);
	}
}
