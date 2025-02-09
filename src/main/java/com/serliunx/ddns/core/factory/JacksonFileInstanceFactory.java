package com.serliunx.ddns.core.factory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.serliunx.ddns.constant.InstanceType;
import com.serliunx.ddns.constant.SystemConstants;
import com.serliunx.ddns.core.instance.Instance;

import java.io.File;

import static com.serliunx.ddns.constant.InstanceClasses.match;

/**
 * Jackson文件实例工厂, 使用jackson的ObjectMapper来分别处理json和xml
 *
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @version 1.0.0
 * @since 2024/5/15
 * @see ObjectMapper
 * @see com.fasterxml.jackson.dataformat.xml.XmlMapper
 * @see com.fasterxml.jackson.databind.json.JsonMapper
 */
public abstract class JacksonFileInstanceFactory extends FileInstanceFactory {

    private final ObjectMapper objectMapper;

    public JacksonFileInstanceFactory(String instanceDir, ObjectMapper objectMapper) {
        super(instanceDir);
        this.objectMapper = objectMapper;
    }

    @Override
    protected Instance loadInstance(File file) {
        try{
            JsonNode root = objectMapper.readTree(file);
            String rootName = root.get(SystemConstants.TYPE_FIELD).asText(); //根据类型去装配实例信息
            InstanceType instanceType = InstanceType.valueOf(rootName);
            return post(objectMapper.treeToValue(root, match(instanceType)));
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    protected abstract String[] fileSuffix();

    /**
     * 处理后续逻辑
     */
    protected abstract Instance post(Instance instance);
}
