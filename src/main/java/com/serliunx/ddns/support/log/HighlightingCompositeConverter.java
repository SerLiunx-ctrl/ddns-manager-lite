package com.serliunx.ddns.support.log;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.pattern.color.ForegroundCompositeConverterBase;

import static ch.qos.logback.core.pattern.color.ANSIConstants.*;

/**
 * 高亮颜色转换器
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @version 1.0.1
 * @since 2024/6/15
 */
public class HighlightingCompositeConverter extends ForegroundCompositeConverterBase<ILoggingEvent> {

	@Override
	protected String getForegroundColorCode(ILoggingEvent event) {
		Level level = event.getLevel();
		switch (level.toInt()) {
			case Level.ERROR_INT:
				return BOLD + RED_FG;
			case Level.WARN_INT:
				return YELLOW_FG;
			case Level.INFO_INT:
				return BLUE_FG;
			case Level.DEBUG_INT:
				return GREEN_FG;
			default:
				return DEFAULT_FG;
		}
	}
}
