package com.serliunx.ddns.support.log;

import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import ch.qos.logback.core.Layout;
import org.jline.reader.LineReader;

/**
 * 适配JLine的控制台日志输出
 *
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @since 2025/1/15
 */
public final class JLineAdaptAppender extends AppenderBase<ILoggingEvent> {

    /**
     * JLine的输入读取
     */
    private static LineReader lineReader;

    /**
     * 格式控制
     */
    private Layout<ILoggingEvent> layout;

    /**
     * 所配置的格式
     */
    private String pattern;

    @Override
    public void start() {
        super.start();
        PatternLayout patternLayout = new PatternLayout();
        patternLayout.setPattern(pattern);
        patternLayout.setContext(getContext());
        patternLayout.start();
        this.layout = patternLayout;
    }

    @Override
    protected void append(ILoggingEvent event) {
        if (lineReader != null) {
            String formattedLog = layout.doLayout(event);
            lineReader.printAbove(formattedLog);
        } else
            System.out.print(layout.doLayout(event));
    }

    /**
     * 设置输入读取
     *
     * @param lr 读取
     */
    public static void setLineReader(LineReader lr) {
        lineReader = lr;
    }

    @SuppressWarnings("all")
    public void setPattern(String pattern) {
        this.pattern = pattern;
    }
}
