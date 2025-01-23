package com.serliunx.ddns.support.command.target;

import com.serliunx.ddns.config.Configuration;
import com.serliunx.ddns.support.SystemInitializer;
import com.serliunx.ddns.support.command.AbstractCommand;
import com.serliunx.ddns.support.ipprovider.ScheduledProvider;

import static com.serliunx.ddns.constant.ConfigurationKeys.KEY_TASK_REFRESH_INTERVAL_IP;

/**
 * 指令: reload
 *
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @version 1.0.4
 * @since 2025/1/16
 */
public class ReloadCommand extends AbstractCommand {

    /**
     * 配置信息
     */
    private final Configuration configuration;
    /**
     * 系统初始化组件
     */
    private final SystemInitializer systemInitializer;

    public ReloadCommand(Configuration configuration, SystemInitializer systemInitializer) {
        super("reload", null, "重新载入配置文件.", "reload");
        this.configuration = configuration;
        this.systemInitializer = systemInitializer;
    }

    @Override
    public boolean onCommand(String[] args) {
        log.info("正在重新载入配置文件...");
        if (configuration == null) {
            return false;
        }
        long oldIpInterval = getIpInterval();
        configuration.refresh();

        // 更新定时查询IP任务
        triggerScheduledProvider(oldIpInterval);

        log.info("配置文件已重新载入!");
        return true;
    }

    /**
     * 获取更新周期
     */
    private long getIpInterval() {
        return configuration.getLong(KEY_TASK_REFRESH_INTERVAL_IP, 300L);
    }

    /**
     * 更新定时查询IP任务
     */
    private void triggerScheduledProvider(long oldIpInterval) {
        final ScheduledProvider scheduledProvider = systemInitializer.getScheduledProvider();
        final long newIpInterval = getIpInterval();
        if (scheduledProvider != null &&
                oldIpInterval != newIpInterval) {
            scheduledProvider.changeTimePeriod(newIpInterval);
        }
    }
}
