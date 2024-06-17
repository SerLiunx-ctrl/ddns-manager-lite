package com.serliunx.ddns;

import com.serliunx.ddns.config.PropertiesConfiguration;
import com.serliunx.ddns.constant.SystemConstants;
import com.serliunx.ddns.core.context.FileInstanceContext;
import com.serliunx.ddns.support.SystemInitializer;

/**
 * 启动类
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @version 1.0.0
 * @since 2024/5/15
 */
public final class ManagerLite {

    public static void main(String[] args) {
        // 容器初始化
        init();
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
}
