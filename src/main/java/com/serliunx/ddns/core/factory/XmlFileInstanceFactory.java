package com.serliunx.ddns.core.factory;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.serliunx.ddns.constant.InstanceSource;
import com.serliunx.ddns.core.instance.Instance;

/**
 * Jackson-Xml文件实例工厂
 * @author SerLiunx
 * @since 1.0
 */
public class XmlFileInstanceFactory extends JacksonFileInstanceFactory{

    public XmlFileInstanceFactory(String instanceDir, XmlMapper xmlMapper) {
        super(instanceDir, xmlMapper);
    }

    public XmlFileInstanceFactory(String instanceDir) {
        this(instanceDir, new XmlMapper());
    }

    @Override
    public int getPriority() {
        return 2;
    }

    @Override
    protected String[] fileSuffix() {
        return new String[]{".xml"};
    }

    @Override
    protected Instance post(Instance instance) {
        instance.setSource(InstanceSource.FILE_XML);
        return instance;
    }
}
