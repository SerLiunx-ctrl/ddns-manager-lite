package com.serliunx.ddns.support;

import com.serliunx.ddns.core.instance.Instance;

/**
 * 实例信息上下文
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @version 1.0.0
 * @since 2024/6/15
 */
public final class InstanceContextHolder {

	/**
	 * 当前线程所涉及的实例信息
	 */
	private static final ThreadLocal<Instance> INSTANCE_THREAD_LOCAL = new ThreadLocal<>();
	/**
	 * 当前线程所需要的附加信息
	 */
	private static final ThreadLocal<String> ADDITIONAL_INFORMATION_THREAD_LOCAL = new ThreadLocal<>();

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
	 * 设置附加信息
	 * @param additional 附加信息
	 */
	public static void setAdditional(String additional) {
		ADDITIONAL_INFORMATION_THREAD_LOCAL.set(additional);
	}

	/**
	 * 获取附加信息
	 * @return 附加信息
	 */
	public static String getAdditional() {
		return ADDITIONAL_INFORMATION_THREAD_LOCAL.get();
	}

	/**
	 * 清除当前线程的实例信息
	 */
	public static void clearInstance() {
		INSTANCE_THREAD_LOCAL.remove();
	}

	/**
	 * 清除当前线程的附加信息
	 */
	public static void clearAdditional() {
		ADDITIONAL_INFORMATION_THREAD_LOCAL.remove();
	}
}
