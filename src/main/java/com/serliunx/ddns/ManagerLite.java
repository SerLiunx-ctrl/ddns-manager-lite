package com.serliunx.ddns;

import com.serliunx.ddns.config.CommandLineConfiguration;
import com.serliunx.ddns.config.Configuration;
import com.serliunx.ddns.config.PropertiesConfiguration;
import com.serliunx.ddns.constant.SystemConstants;
import com.serliunx.ddns.core.context.FileInstanceContext;
import com.serliunx.ddns.core.context.MultipleSourceInstanceContext;
import com.serliunx.ddns.support.SystemInitializer;

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

    public static void main(String[] args) {

        // 配置初始化
        initConfiguration(args);

        // 初始化实例容器
        initContext();

        // 系统初始化
        initSystem();
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
