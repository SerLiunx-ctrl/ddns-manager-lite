package com.serliunx.ddns.support.feign;

import com.fasterxml.jackson.databind.*;
import feign.FeignException;
import feign.Response;
import feign.Util;
import feign.codec.Decoder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.Collections;

/**
 * feign解码器
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @version 1.0.0
 * @since 2024/5/15
 */
public class JacksonDecoder implements Decoder {

    private final ObjectMapper mapper;
    private static final JacksonDecoder decoder = new JacksonDecoder();

    private JacksonDecoder() {
        this(Collections.emptyList());
    }

    private JacksonDecoder(Iterable<Module> modules) {
        this(new ObjectMapper()
                //设置下划线自动转化为驼峰命名
                .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .registerModules(modules));
    }

    private JacksonDecoder(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public static Decoder getInstance() {
        return decoder;
    }

    @Override
    public Object decode(Response response, Type type) throws FeignException, IOException {
        if (response.status() == 404 || response.status() == 204)
            return Util.emptyValueOf(type);
        if (response.body() == null)
            return null;
        Reader reader = response.body().asReader(response.charset());
        if (!reader.markSupported()) {
            reader = new BufferedReader(reader, 1);
        }
        //处理响应体字符流
        try{
            reader.mark(1);
            if (reader.read() == -1) {
                return null;
            }
            reader.reset();
            return mapper.readValue(reader, mapper.constructType(type));
        } catch (RuntimeJsonMappingException e) {
            if (e.getCause() != null && e.getCause() instanceof IOException) {
                throw (IOException) e.getCause();
            }
            throw e;
        }finally {
            response.close();
        }
    }
}
