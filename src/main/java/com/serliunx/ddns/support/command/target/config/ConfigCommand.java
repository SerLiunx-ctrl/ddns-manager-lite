package com.serliunx.ddns.support.command.target.config;

import com.serliunx.ddns.config.Configuration;
import com.serliunx.ddns.support.command.AbstractCommand;

import java.util.ArrayList;

/**
 * 指令: config
 *
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @version 1.0.4
 * @since 2025/1/22
 */
public class ConfigCommand extends AbstractCommand {

    public ConfigCommand(Configuration configuration) {
        super("config", new ArrayList<>(), "调整配置信息", "config <get/set/...>");
        // 子命令: set
        addSubCommand(new ConfigSetCommand(configuration));
        // 子命令: get
        addSubCommand(new ConfigGetCommand(configuration));
    }
}
