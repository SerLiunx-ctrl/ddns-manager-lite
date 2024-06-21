package com.serliunx.ddns.support.log;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import com.serliunx.ddns.core.instance.Instance;
import com.serliunx.ddns.support.InstanceContextHolder;

/**
 * 获取当前任何线程的实例信息或者附加信息
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @version 1.0.0
 * @since 2024/6/15
 */
public class InstanceNameConverter extends ClassicConverter {

	@Override
	public String convert(ILoggingEvent event) {
		Instance instance = InstanceContextHolder.getInstance();
		String content;
		if (instance != null) {
			content = instance.getName();
		} else {
			content = InstanceContextHolder.getAdditional();
			return content == null ? "----" : content;
		}
		return content;
	}
}
