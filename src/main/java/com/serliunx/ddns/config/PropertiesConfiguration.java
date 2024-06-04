package com.serliunx.ddns.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * 使用{@link Properties}实现的简单读取键值对形式的配置信息实现
 * @author SerLiunx
 * @since 1.0
 */
public class PropertiesConfiguration extends FileConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(PropertiesConfiguration.class);

    private Properties properties;

    public PropertiesConfiguration(String path) {
        super(path);
    }

    @Override
    public int getPriority() {
        return 1;
    }

    @Override
    protected void refresh0() {
        this.properties = new Properties();
        InputStream inputStream = null;
        try {
            inputStream = Files.newInputStream(Paths.get(path));
            properties.load(inputStream);
            // 载入配置信息
            load();
        } catch (IOException e) {
            LOGGER.error("配置文件读取出现异常 => {}", e.toString());
        }finally {
            if(inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    LOGGER.error("配置文件资源释放出现异常 => {}", e.getMessage());
                }
            }
        }
    }

    @Override
    protected void load0() {
        Set<Map.Entry<Object, Object>> entries = properties.entrySet();
        entries.forEach(e -> {
            valueMap.put((String) e.getKey(), (String) e.getValue());
        });
    }
}
