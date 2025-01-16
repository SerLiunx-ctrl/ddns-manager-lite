package com.serliunx.ddns.support.command.target;

import com.serliunx.ddns.ManagerLite;
import com.serliunx.ddns.config.Configuration;
import com.serliunx.ddns.support.command.AbstractCommand;
import org.slf4j.Logger;

/**
 * 指令: reload
 *
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @version 1.0.4
 * @since 2025/1/16
 */
public class ReloadCommand extends AbstractCommand {

    private static final Logger log = ManagerLite.getLogger();

    /**
     * 配置信息
     */
    private final Configuration configuration;

    public ReloadCommand(Configuration configuration) {
        super("reload", null, "重新载入配置文件.", "reload");
        this.configuration = configuration;
    }

    @Override
    public boolean onCommand(String[] args) {
        if (configuration == null) {
            return false;
        }
        configuration.refresh();
        return true;
    }
}
