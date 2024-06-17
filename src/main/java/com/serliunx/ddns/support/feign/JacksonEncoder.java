package com.serliunx.ddns.support.feign;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import feign.RequestTemplate;
import feign.Util;
import feign.codec.EncodeException;
import feign.codec.Encoder;

import java.lang.reflect.Type;
import java.util.Collections;

/**
 * Feign兼容Jackson(反序列化返回值)
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @version 1.0.0
 * @since 2024/5/15
 */
public class JacksonEncoder implements Encoder {

    private final ObjectMapper mapper;
    private static final JacksonEncoder encoder = new JacksonEncoder();

    private JacksonEncoder() {
        this(Collections.emptyList());
    }

    private JacksonEncoder(Iterable<Module> modules) {
        this(new ObjectMapper()
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .configure(SerializationFeature.INDENT_OUTPUT, true)
                .registerModules(modules));
    }

    private JacksonEncoder(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public static Encoder getInstance() {
        return encoder;
    }

    @Override
    public void encode(Object object, Type bodyType, RequestTemplate template) {
        try {
            JavaType javaType = mapper.getTypeFactory().constructType(bodyType);
            template.body(mapper.writerFor(javaType).writeValueAsBytes(object), Util.UTF_8);
        } catch (JsonProcessingException e) {
            throw new EncodeException(e.getMessage(), e);
        }
    }
}
