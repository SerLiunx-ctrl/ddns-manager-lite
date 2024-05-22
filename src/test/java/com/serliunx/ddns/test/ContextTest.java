package com.serliunx.ddns.test;

import com.serliunx.ddns.constant.SystemConstants;
import com.serliunx.ddns.core.context.FileInstanceContext;
import com.serliunx.ddns.core.context.GenericInstanceContext;
import com.serliunx.ddns.core.context.MultipleSourceInstanceContext;
import com.serliunx.ddns.core.factory.JsonFileInstanceFactory;
import com.serliunx.ddns.core.factory.XmlFileInstanceFactory;
import com.serliunx.ddns.core.factory.YamlFileInstanceFactory;
import org.junit.Test;

/**
 * @author SerLiunx
 * @since 1.0
 */
public class ContextTest {

    @Test
    public void testGenericContext() {
        GenericInstanceContext genericInstanceContext = new GenericInstanceContext();

        genericInstanceContext.addListableInstanceFactory(new XmlFileInstanceFactory(SystemConstants.USER_INSTANCE_DIR));
        genericInstanceContext.addListableInstanceFactory(new YamlFileInstanceFactory(SystemConstants.USER_INSTANCE_DIR));
        genericInstanceContext.addListableInstanceFactory(new JsonFileInstanceFactory(SystemConstants.USER_INSTANCE_DIR));

        genericInstanceContext.refresh();
        genericInstanceContext.getInstances().forEach(System.out::println);
    }

    @Test
    public void testFileContext(){
        MultipleSourceInstanceContext context = new FileInstanceContext();

        context.getInstances().forEach(System.out::println);
    }
}
