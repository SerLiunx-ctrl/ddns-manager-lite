package com.serliunx.ddns.core.factory;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.serliunx.ddns.constant.InstanceSource;
import com.serliunx.ddns.core.instance.Instance;

/**
 * Jackson-Json文件实例工厂
 * @author SerLiunx
 * @since 1.0
 */
public class JsonFileInstanceFactory extends JacksonFileInstanceFactory{

    public JsonFileInstanceFactory(String instanceDir, JsonMapper jsonMapper) {
        super(instanceDir, jsonMapper);
    }

    public JsonFileInstanceFactory(String instanceDir) {
        this(instanceDir, new JsonMapper());
    }

    @Override
    public int getPriority() {
        return 1;
    }

    @Override
    protected String[] fileSuffix() {
        return new String[]{".json"};
    }

    @Override
    protected Instance post(Instance instance) {
        instance.setSource(InstanceSource.FILE_JSON);
        return instance;
    }
}
