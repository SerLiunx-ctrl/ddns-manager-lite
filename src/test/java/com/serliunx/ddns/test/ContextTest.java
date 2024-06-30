package com.serliunx.ddns.test;

import com.serliunx.ddns.constant.InstanceType;
import com.serliunx.ddns.constant.SystemConstants;
import com.serliunx.ddns.core.context.FileInstanceContext;
import com.serliunx.ddns.core.context.GenericInstanceContext;
import com.serliunx.ddns.core.context.MultipleSourceInstanceContext;
import com.serliunx.ddns.core.factory.JsonFileInstanceFactory;
import com.serliunx.ddns.core.factory.XmlFileInstanceFactory;
import com.serliunx.ddns.core.factory.YamlFileInstanceFactory;
import com.serliunx.ddns.core.instance.AliyunInstance;
import com.serliunx.ddns.core.instance.Instance;
import org.junit.Test;

/**
 * @author SerLiunx
 * @since 1.0
 */
public class ContextTest {

    @Test
    public void testGenericContext() {
        GenericInstanceContext genericInstanceContext = new GenericInstanceContext(true);

        genericInstanceContext.addListableInstanceFactory(new XmlFileInstanceFactory(SystemConstants.USER_INSTANCE_DIR));
        genericInstanceContext.addListableInstanceFactory(new YamlFileInstanceFactory(SystemConstants.USER_INSTANCE_DIR));
        genericInstanceContext.addListableInstanceFactory(new JsonFileInstanceFactory(SystemConstants.USER_INSTANCE_DIR));

        genericInstanceContext.refresh();
        genericInstanceContext.getInstances().forEach(System.out::println);

        genericInstanceContext.clear();
    }

    @Test
    public void testFileContext(){
        FileInstanceContext context = new FileInstanceContext();
        context.getSortedListableInstanceFactories().forEach(System.out::println);
    }

    @Test
    public void testEmptyContext(){
        GenericInstanceContext instanceContext = new GenericInstanceContext(false);
        instanceContext.addListableInstanceFactory(new YamlFileInstanceFactory(SystemConstants.USER_INSTANCE_DIR));

        Instance instance = new AliyunInstance();
        instance.setName("huawei2");
        instance.setType(InstanceType.ALI_YUN);

        instanceContext.refresh();
        instanceContext.addInstance(instance);

        instanceContext.getInstances().forEach(System.out::println);
    }
}
