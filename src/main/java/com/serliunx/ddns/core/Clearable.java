package com.serliunx.ddns.core;

/**
 * 定义一个实体的清理逻辑
 * <li> 一般用来清理中间加载过程中所产生的无用对象
 *
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @version 1.0.0
 * @since 2024/5/15
 */
@FunctionalInterface
public interface Clearable {

	/**
	 * 执行清理
	 */
	void clear();

	/**
	 * 实体是否需要执行清理逻辑
	 * <li> 默认为真
	 * @return 是否需要执行清理逻辑
	 */
	default boolean isClearable() {
		return true;
	}
}
