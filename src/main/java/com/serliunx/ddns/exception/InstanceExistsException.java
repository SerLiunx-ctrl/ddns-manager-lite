package com.serliunx.ddns.exception;

import com.serliunx.ddns.core.instance.Instance;

/**
 * 异常信息, 实例已存在
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @since 1.0
 */
public class InstanceExistsException extends InstanceException {

	private final Instance existsInstance;

	public InstanceExistsException(String message, String instanceName, Instance existsInstance) {
		super(message, instanceName);
		this.existsInstance = existsInstance;
	}

	public Instance getExistsInstance() {
		return existsInstance;
	}
}
