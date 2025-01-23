package com.serliunx.ddns.support.command.target;

import com.serliunx.ddns.config.Configuration;
import com.serliunx.ddns.support.command.AbstractCommand;

/**
 * 指令: config
 *
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @version 1.0.4
 * @since 2025/1/22
 */
public class ConfigCommand extends AbstractCommand {

    /**
     * 配置信息
     */
    private final Configuration configuration;

    public ConfigCommand(Configuration configuration) {
        super("config", null, "调整配置信息", "config <配置项> 新的值");
        this.configuration = configuration;
    }

    @Override
    public boolean onCommand(String[] args) {
        if (!hasArgs(args) ||
                args.length < 2) {
            log.warn("用法 => {}", getUsage());
            return true;
        }
        final String target = args[0];
        final String value = args[1];
        return configuration.modify(target, value);
    }
}
