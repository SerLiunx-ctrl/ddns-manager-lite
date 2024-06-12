package com.serliunx.ddns;

import com.serliunx.ddns.config.PropertiesConfiguration;
import com.serliunx.ddns.constant.SystemConstants;
import com.serliunx.ddns.core.context.FileInstanceContext;
import com.serliunx.ddns.support.SystemInitializer;
import com.serliunx.ddns.support.SystemSupport;
import com.serliunx.ddns.support.command.CommandManager;
import com.serliunx.ddns.support.command.cmd.ExitCommand;
import com.serliunx.ddns.support.command.cmd.HelpCommand;
import com.serliunx.ddns.support.command.cmd.IpCommand;
import org.slf4j.MDC;

import java.util.Scanner;

/**
 * 启动类
 * @author SerLiunx
 * @since 1.0
 */
public final class ManagerLite {

    public static void main(String[] args) {
        // 日志参数调整
        beforeInit();

        // 容器初始化
        SystemInitializer initializer = init();

        // 指令注册
        CommandManager commandManager = registerCommand(initializer);

        // 指令监听
        handleCommand(commandManager, initializer);
    }

    private static void beforeInit() {
        MDC.put("pid", SystemSupport.getPid());
    }

    private static SystemInitializer init() {
        SystemInitializer systemInitializer = SystemInitializer
                .configurer()
                .configuration(new PropertiesConfiguration(SystemConstants.USER_SETTINGS_PROPERTIES_PATH))
                .instanceContext(new FileInstanceContext())
                .done();
        systemInitializer.refresh();
        return systemInitializer;
    }

    private static CommandManager registerCommand(SystemInitializer systemInitializer) {
        CommandManager commandManager = new CommandManager(systemInitializer);
        commandManager.register(new HelpCommand(commandManager));
        commandManager.register(new IpCommand(commandManager));
        commandManager.register(new ExitCommand(commandManager));
        return commandManager;
    }

    @SuppressWarnings("all")
    private static void handleCommand(CommandManager commandManager, SystemInitializer systemInitializer) {
        Scanner scanner = new Scanner(System.in);
        String commandString = "";

        while (true) {
            System.out.print("> ");
            commandString = scanner.nextLine();
            commandManager.handle(commandString);
        }
    }
}
