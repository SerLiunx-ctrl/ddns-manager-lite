package com.serliunx.ddns.test;

import com.serliunx.ddns.constant.SystemConstants;
import com.serliunx.ddns.core.factory.FileInstanceFactory;
import com.serliunx.ddns.core.factory.YamlFileInstanceFactory;
import org.junit.Test;

import java.io.File;
import java.util.Collection;

/**
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @version 1.0.0
 * @since 2024/8/6
 */
public class AttachmentTest {

	@Test
	public void testAttachment() {
		FileInstanceFactory fileInstanceFactory =  new YamlFileInstanceFactory(SystemConstants.USER_INSTANCE_DIR);
		fileInstanceFactory.refresh();

		Collection<File> files = fileInstanceFactory.getAttachments();
		files.forEach(f -> System.out.println(f.getName()));
	}
}
