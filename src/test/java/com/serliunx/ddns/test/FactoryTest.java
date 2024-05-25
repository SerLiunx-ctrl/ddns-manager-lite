package com.serliunx.ddns.test;

import com.serliunx.ddns.constant.SystemConstants;
import com.serliunx.ddns.core.factory.YamlFileInstanceFactory;
import org.junit.Test;

/**
 * @author SerLiunx
 * @since 1.0
 */
public class FactoryTest {

    @Test
    public void testYamlFileFactory() {
        YamlFileInstanceFactory factory = new YamlFileInstanceFactory(SystemConstants.USER_INSTANCE_DIR);
        factory.refresh();
        factory.getInstances().forEach(System.out::println);
        factory.clear();
    }
}
