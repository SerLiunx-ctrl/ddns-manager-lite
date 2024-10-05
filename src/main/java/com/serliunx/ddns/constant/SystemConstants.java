package com.serliunx.ddns.constant;

import java.io.File;

/**
 * 系统常量
 *
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @version 1.0.0
 * @since 2024/5/15
 */
public final class SystemConstants {

    private SystemConstants(){throw new UnsupportedOperationException();}

    /**
     * 保存实例的文件夹
     */
    public static final String INSTANCE_FOLDER_NAME = "instances";

    /**
     * 运行目录
     */
    public static final String USER_DIR = System.getProperty("user.dir");

    /**
     * JSON文件后缀
     */
    public static final String JSON_FILE = ".json";

    /**
     * XML文件后缀
     */
    public static final String XML_FILE = ".xml";

    /**
     * YML文件后缀
     */
    public static final String YML = ".yml";

    /**
     * properties配置文件名称
     */
    public static final String CONFIG_PROPERTIES_FILE = "settings.properties";

    /**
     * yaml配置文件名称
     */
    public static final String CONFIG_YAML_FILE = "settings.yml";

    /**
     * 日志配置文件名称
     */
    public static final String LOG_CONFIG_FILE = "logback.xml";

    /**
     * sqlite
     */
    public static final String DATABASE_SQLITE = "sqlite";

    /**
     * XML格式的实例文件根元素名称
     */
    public static final String XML_ROOT_INSTANCE_NAME = "instance";

    /**
     * 实例类型字段名
     */
    public final static String TYPE_FIELD = "type";

    /**
     * 用户目录下的实例存放位置
     */
    public static final String USER_INSTANCE_DIR = USER_DIR + File.separator + INSTANCE_FOLDER_NAME;

    /**
     * 用户目录下的.properties配置文件
     */
    public static final String USER_SETTINGS_PROPERTIES_PATH = USER_DIR + File.separator + CONFIG_PROPERTIES_FILE;

    /**
     * 用户目录下的.yml配置文件
     */
    public static final String USER_SETTINGS_YAML_PATH = USER_DIR + File.separator + CONFIG_YAML_FILE;
}
