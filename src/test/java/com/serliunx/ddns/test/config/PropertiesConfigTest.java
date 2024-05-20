package com.serliunx.ddns.test.config;

import com.serliunx.ddns.config.Configuration;
import com.serliunx.ddns.config.PropertiesConfiguration;
import com.serliunx.ddns.constant.InstanceSource;
import com.serliunx.ddns.constant.SystemConstants;
import org.junit.Test;

public class PropertiesConfigTest {

    @Test
    public void test() {
        Configuration configuration = new PropertiesConfiguration(SystemConstants.USER_SETTINGS_PROPERTIES_PATH);
        configuration.refresh();

        InstanceSource test = configuration.getEnum(InstanceSource.class, "test", InstanceSource.FILE_JSON);
        System.out.println(test);
    }
}
