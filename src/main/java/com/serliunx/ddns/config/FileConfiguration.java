package com.serliunx.ddns.config;

/**
 * 文件配置管理
 * @see PropertiesConfiguration
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @version 1.0.0
 * @since 2024/5/15
 */
public abstract class FileConfiguration extends AbstractConfiguration {

	protected final String path;

	public FileConfiguration(String path) {
		this.path = path;
	}

	public String getPath() {
		return path;
	}
}
