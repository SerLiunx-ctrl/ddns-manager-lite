package com.serliunx.ddns.constant;

/**
 * 保存实例的文件类型: XML、JSON等
 * @author SerLiunx
 * @since 1.0
 */
public enum InstanceFileType {
    XML(".xml"),
    JSON(".json"),
    YML(".yml"),
    YAML(".yaml"),
    ;

    private final String value;

    public String getValue() {
        return value;
    }

    InstanceFileType(String value) {
        this.value = value;
    }
}
