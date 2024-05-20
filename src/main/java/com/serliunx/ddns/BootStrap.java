package com.serliunx.ddns;

import com.serliunx.ddns.config.PropertiesConfiguration;
import com.serliunx.ddns.constant.SystemConstants;
import com.serliunx.ddns.core.context.FileInstanceContext;
import com.serliunx.ddns.support.SystemInitializer;
import com.serliunx.ddns.support.SystemSupport;
import org.slf4j.MDC;

/**
 * 启动类
 * @author SerLiunx
 * @since 1.0
 */
public final class BootStrap {

    public static void main(String[] args) {
        beforeInit();
        init();
    }

    private static void beforeInit() {
        MDC.put("pid", SystemSupport.getPid());
    }

    private static void init() {
        SystemInitializer systemInitializer = SystemInitializer
                .configurer()
                .configuration(new PropertiesConfiguration(SystemConstants.USER_SETTINGS_PROPERTIES_PATH))
                .instanceContext(new FileInstanceContext())
                .done();
        systemInitializer.refresh();
    }
}
