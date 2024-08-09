package com.serliunx.ddns.core;

import java.util.Collection;

/**
 * 附件定义
 *
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @version 1.0.0
 * @since 2024/8/6
 */
public interface Attachment<E> {

	/**
	 * 是否存在指定附件
	 *
	 * @param e 附件信息
	 * @return 存在返回真, 否则返回假
	 */
	boolean exists(E e);

	/**
	 * 分离指定附件
	 *
	 * @param e 附件
	 * @return 成功分离返回真, 否则返回假(不存在时返回假)
	 */
	boolean detach(E e);

	/**
	 * 添加一个附件
	 *
	 * @param e 附件
	 */
	void attach(E e);

	/**
	 * 获取所有附件对象
	 *
	 * @return 所有附件对象
	 */
	Collection<E> getAttachments();
}
