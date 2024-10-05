package com.serliunx.ddns.core.factory;

import com.serliunx.ddns.constant.InstanceClasses;
import com.serliunx.ddns.constant.InstanceSource;
import com.serliunx.ddns.constant.InstanceType;
import com.serliunx.ddns.core.instance.Instance;
import com.serliunx.ddns.util.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;

import static com.serliunx.ddns.constant.SystemConstants.TYPE_FIELD;

/**
 * Yaml文件实例工厂, 使用SnakeYaml来反序列化实例. 属于文件型实例工厂.
 *
 * @see FileInstanceFactory
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @version 1.0.0
 * @since 2024/5/15
 */
public class YamlFileInstanceFactory extends FileInstanceFactory {

    private static final Logger logger = LoggerFactory.getLogger(YamlFileInstanceFactory.class);

    public YamlFileInstanceFactory(String instanceDir) {
        super(instanceDir);
    }

    @Override
    public int getPriority() {
        return 3;
    }

    @Override
    protected Instance loadInstance(File file) {
        FileInputStream instanceInputStream = null;
        try {
            instanceInputStream = new FileInputStream(file);
            Yaml yaml = new Yaml();
            Map<String, Object> valueMap = yaml.load(instanceInputStream);
            InstanceType type = null;
            if (valueMap.get(TYPE_FIELD) != null) {
                type = InstanceType.valueOf((String) valueMap.get(TYPE_FIELD));
            }
            if (type == null) {
                logger.error("文件 {} 读取失败, 可能是缺少关键参数.", file.getName());
                return null;
            }
            Class<? extends Instance> clazz = InstanceClasses.match(type);
            if (clazz != null) {
                Constructor<? extends Instance> constructor = clazz.getConstructor();
                Instance instance = buildInstance(constructor.newInstance(), valueMap);
                instance.setSource(InstanceSource.FILE_YML);
                return instance;
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (instanceInputStream != null) {
                    instanceInputStream.close();
                }
            } catch (IOException e) {
                logger.error("文件读取出现异常.");
            }
        }
    }

    @Override
    protected String[] fileSuffix() {
        return new String[]{".yml", ".yaml"};
    }

    @SuppressWarnings(value = {"unchecked", "rawtypes"})
    protected Instance buildInstance(Instance instance, Map<String, Object> valueMap) {
        Field[] declaredFields = ReflectionUtils.getDeclaredFields(instance.getClass(), true);
        for (Field f : declaredFields) {
            if (Modifier.isStatic(f.getModifiers())) {
                continue;
            }
            Object value = valueMap.get(f.getName());
            f.setAccessible(true);
            try {
                //设置枚举类
                Class<?> clazz = f.getType();
                if (clazz.isEnum() && value != null) {
                    f.set(instance, Enum.valueOf((Class<? extends Enum>) clazz, (String) value));
                    continue;
                }
                if (value != null) {
                    f.set(instance, value);
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } finally {
                f.setAccessible(false);
            }
        }
        return instance;
    }
}
