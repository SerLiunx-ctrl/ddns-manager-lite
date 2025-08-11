package com.serliunx.ddns.config;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * 使用{@link Properties}实现的简单读取键值对形式的配置信息实现
 *
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @version 1.0.0
 * @since 2024/5/15
 */
public class PropertiesConfiguration extends FileConfiguration {

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
        try (InputStream inputStream = Files.newInputStream(Paths.get(path))) {
            properties.load(inputStream);
            // 载入配置信息
            load();
        } catch (IOException e) {
            log.error("配置文件读取出现异常 => {}", e.toString());
        }
    }

    @Override
    protected void load0() {
        Set<Map.Entry<Object, Object>> entries = properties.entrySet();
        entries.forEach(e -> valueMap.put((String) e.getKey(), (String) e.getValue()));
    }
}
