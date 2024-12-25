package com.serliunx.ddns.test.config;

import com.serliunx.ddns.config.CommandLineConfiguration;
import com.serliunx.ddns.config.PropertiesConfiguration;
import com.serliunx.ddns.constant.SystemConstants;
import org.junit.Test;

import java.util.Collections;

/**
 * 命令行配置读取测试
 *
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @since 2024/12/24
 */
public class CmdConfigurationTest {

    @Test
    public void testCmd() {
        CommandLineConfiguration configuration = new CommandLineConfiguration(new String[]{"-Dtest.env=1",
                "-Dsystem.cfg.log.onstart=false", "-Dapplication.name=jack"},
                Collections.singleton(new PropertiesConfiguration(SystemConstants.USER_SETTINGS_PROPERTIES_PATH)));
        configuration.refresh();
    }
}
