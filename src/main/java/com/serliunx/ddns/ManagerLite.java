package com.serliunx.ddns;

import com.serliunx.ddns.config.CommandLineConfiguration;
import com.serliunx.ddns.config.Configuration;
import com.serliunx.ddns.config.PropertiesConfiguration;
import com.serliunx.ddns.constant.SystemConstants;
import com.serliunx.ddns.core.context.FileInstanceContext;
import com.serliunx.ddns.core.context.MultipleSourceInstanceContext;
import com.serliunx.ddns.support.SystemInitializer;
import com.serliunx.ddns.support.log.JLineAdaptAppender;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.impl.history.DefaultHistory;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

/**
 * 启动类
 *
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @version 1.0.0
 * @since 2024/5/15
 */
public final class ManagerLite {

    /**
     * 配置信息
     */
    private static Configuration configuration;
    /**
     * 实例容器
     */
    private static MultipleSourceInstanceContext instanceContext;
    /**
     * 系统初始化器
     */
    private static SystemInitializer systemInitializer;

    public static void main(String[] args) throws Exception {

        // 配置初始化
        initConfiguration(args);

        // 初始化实例容器
        initContext();

        // 系统初始化
        initSystem();

        Terminal terminal = TerminalBuilder.builder().system(true).build();
        LineReader lineReader = LineReaderBuilder.builder()
                .terminal(terminal)
                // 如果想记录历史命令，可以配置一个 History
                .history(new DefaultHistory())
                .build();

        JLineAdaptAppender.setLineReader(lineReader);

        String prompt = "client> ";

        while (true) {
            // 该方法会阻塞，直到用户敲回车
            String line = lineReader.readLine(prompt);

            // 当用户输入 exit 或 quit，就退出循环
            if ("exit".equalsIgnoreCase(line)
                    || "quit".equalsIgnoreCase(line)) {
                break;
            }
            // 在这里可以对用户输入做进一步处理
            terminal.writer().println("You entered: " + line);
            terminal.flush();
        }

        terminal.close();
    }

    /**
     * 初始化实例容器
     */
    private static void initContext() {
        instanceContext = new FileInstanceContext();
    }

    /**
     * 配置初始化
     */
    private static void initConfiguration(String[] args) {
        final CommandLineConfiguration cc = new CommandLineConfiguration(args);
        cc.from(new PropertiesConfiguration(SystemConstants.USER_SETTINGS_PROPERTIES_PATH));
        configuration = cc;
    }

    /**
     * 系统初始化
     */
    private static void initSystem() {
        systemInitializer = SystemInitializer
                .configurer()
                .clearCache(false)
                .configuration(configuration)
                .instanceContext(instanceContext)
                .done();
        systemInitializer.refresh();
    }
}
