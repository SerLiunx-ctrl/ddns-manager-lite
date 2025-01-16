package com.serliunx.ddns;

import com.serliunx.ddns.config.CommandLineConfiguration;
import com.serliunx.ddns.config.Configuration;
import com.serliunx.ddns.config.PropertiesConfiguration;
import com.serliunx.ddns.constant.SystemConstants;
import com.serliunx.ddns.core.context.FileInstanceContext;
import com.serliunx.ddns.core.context.MultipleSourceInstanceContext;
import com.serliunx.ddns.support.InstanceContextHolder;
import com.serliunx.ddns.support.SystemInitializer;
import com.serliunx.ddns.support.command.CommandDispatcher;
import com.serliunx.ddns.support.command.target.HelpCommand;
import com.serliunx.ddns.support.command.target.ReloadCommand;
import com.serliunx.ddns.support.log.JLineAdaptAppender;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.impl.history.DefaultHistory;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * 启动类
 *
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @version 1.0.0
 * @since 2024/5/15
 */
public final class ManagerLite {

    /**
     * 默认的日志输出
     */
    private static final Logger DEFAULT_LOGGER = LoggerFactory.getLogger(ManagerLite.class);

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
    /**
     * 指令调度
     */
    private static CommandDispatcher commandDispatcher;

    /**
     * 获取默认的日志输出
     */
    public static Logger getLogger() {
        return DEFAULT_LOGGER;
    }

    public static void main(String[] args) {
        // 配置初始化
        initConfiguration(args);

        // 初始化实例容器
        initContext();

        // 系统初始化
        initSystem();

        // 指令初始化
        initCommands();

        Terminal terminal;
        try {
            terminal = TerminalBuilder.builder()
                    .system(true)
                    .build();
        } catch (IOException e) {
            // 不应该发生
            System.exit(0);
            throw new RuntimeException(e);
        }
        LineReader lineReader = LineReaderBuilder.builder()
                .terminal(terminal)
                // 如果想记录历史命令，可以配置一个 History
                .history(new DefaultHistory())
                .build();

        JLineAdaptAppender.setLineReader(lineReader);

        final String prompt = "client> ";

        InstanceContextHolder.setAdditional("command-process");
        while (true) {
            // 该方法会阻塞，直到用户敲回车
            try {
                String cmd = lineReader.readLine(prompt);

                // 当用户输入 exit，就退出循环
                if ("exit".equalsIgnoreCase(cmd)) {
                    break;
                }
                commandDispatcher.onCommand(cmd);
                terminal.flush();
            } catch (Exception e) {
                break;
            }
        }

        System.exit(0);
    }

    /**
     * 指令初始化
     */
    private static void initCommands() {
        commandDispatcher = CommandDispatcher.getInstance();
        // help
        commandDispatcher.register(new HelpCommand());
        // reload
        commandDispatcher.register(new ReloadCommand(configuration));
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
