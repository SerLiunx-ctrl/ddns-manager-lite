package com.serliunx.ddns.exception;

/**
 * 实例相关异常信息
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @version 1.0.0
 * @since 2024/5/15
 */
public abstract class InstanceException extends RuntimeException {

	private final String instanceName;

	public InstanceException() {
		this.instanceName = null;
	}

	public InstanceException(String message, String instanceName) {
		super(message);
		this.instanceName = instanceName;
	}

	public String getInstanceName() {
		return instanceName;
	}
}
