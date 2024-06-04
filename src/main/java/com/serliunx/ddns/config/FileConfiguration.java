package com.serliunx.ddns.config;

/**
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @since 1.0
 */
public abstract class FileConfiguration extends AbstractConfiguration {

	protected final String path;

	public FileConfiguration(String path) {
		this.path = path;
	}
}
