package com.serliunx.ddns.support.log;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

import java.lang.management.ManagementFactory;

/**
 * 日志变量%pid(进程id) 转换器
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @version 1.0.0
 * @since 2024/6/15
 */
public class ProcessIdConverter extends ClassicConverter {

	private static final String PROCESS_ID = ManagementFactory.getRuntimeMXBean().getName().split("@")[0];

	@Override
	public String convert(ILoggingEvent iLoggingEvent) {
		return PROCESS_ID;
	}
}
