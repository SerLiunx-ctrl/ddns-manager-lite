package com.serliunx.ddns.core;

import java.io.File;
import java.util.Collection;

/**
 * 文件类型的附件
 *
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @version 1.0.0
 * @since 2024/8/6
 */
public interface FileAttachment extends Attachment<File> {

	/**
	 * 检查所有文件附件是否都是文件夹
	 */
	default boolean isAllDirectories() {
		Collection<File> files = getAttachments();
		if (files == null || files.isEmpty())
			return false;

		for (File file : files) {
			if (!file.isDirectory())
				return false;
		}
		return true;
	}
}
