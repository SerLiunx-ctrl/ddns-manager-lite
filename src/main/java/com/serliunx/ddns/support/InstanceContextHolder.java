package com.serliunx.ddns.support;

import com.serliunx.ddns.core.instance.Instance;

/**
 * 实例信息上下文
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @version 1.0.0
 * @since 2024/6/15
 */
public final class InstanceContextHolder {

	private static final ThreadLocal<Instance> INSTANCE_THREAD_LOCAL = new ThreadLocal<>();

	private InstanceContextHolder() {throw new UnsupportedOperationException();}

	/**
	 * 设置当前线程的实例信息
	 * @param instance 实例
	 */
	public static void setInstance(Instance instance) {
		INSTANCE_THREAD_LOCAL.set(instance);
	}

	/**
	 * 获取当前线程的实例信息
	 * @return 实例信息
	 */
	public static Instance getInstance() {
		return INSTANCE_THREAD_LOCAL.get();
	}

	/**
	 * 清除当前线程的实例信息
	 */
	public static void clear() {
		INSTANCE_THREAD_LOCAL.remove();
	}
}
