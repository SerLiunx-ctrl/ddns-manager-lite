package com.serliunx.ddns.test;

import com.serliunx.ddns.constant.InstanceType;
import com.serliunx.ddns.constant.SystemConstants;
import com.serliunx.ddns.core.factory.InstanceFactory;
import com.serliunx.ddns.core.factory.ListableInstanceFactory;
import com.serliunx.ddns.core.factory.YamlFileInstanceFactory;
import com.serliunx.ddns.core.instance.Instance;
import org.junit.Test;

import java.util.Map;

/**
 * @author SerLiunx
 * @since 1.0
 */
public class FactoryTest {

    @Test
    public void testYamlFileFactory() {
        ListableInstanceFactory factory = new YamlFileInstanceFactory(SystemConstants.USER_INSTANCE_DIR);
        factory.refresh();
        factory.getInstances().forEach(System.out::println);

        Map<String, Instance> instances = factory.getInstanceOfType(InstanceType.TENCENT_CLOUD);

        instances.forEach((k, v) -> {
            System.out.println(k + ": " + v);
        });
    }
}
