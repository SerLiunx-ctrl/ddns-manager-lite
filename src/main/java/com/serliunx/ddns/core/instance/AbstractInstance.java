package com.serliunx.ddns.core.instance;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.serliunx.ddns.constant.InstanceSource;
import com.serliunx.ddns.constant.InstanceType;

import static com.serliunx.ddns.constant.SystemConstants.XML_ROOT_INSTANCE_NAME;

/**
 * @author SerLiunx
 * @since 1.0
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JacksonXmlRootElement(localName = XML_ROOT_INSTANCE_NAME)
public abstract class AbstractInstance implements Instance {

    /**
     * 实例名称
     * <li> 全局唯一
     */
    protected String name;

    /**
     * 父实例名称
     */
    protected String fatherName;

    /**
     * 执行周期
     */
    protected Long interval;

    /**
     * 实例类型
     */
    protected InstanceType type;

    /**
     * 实例来源
     */
    protected InstanceSource source;

    /**
     * 获取到的ip地址. 仅做记录, 不需要手动设定
     */
    protected String value;

    @Override
    public void refresh() {
        // 调用子类的初始化逻辑
        init();
    }

    @Override
    public void run() {
        if(query())
            run0();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public Long getInterval() {
        return interval;
    }

    public void setInterval(Long interval) {
        this.interval = interval;
    }

    @Override
    public InstanceType getType() {
        return type;
    }

    @Override
    public void setType(InstanceType instanceType) {
        this.type = instanceType;
    }

    @Override
    public void setSource(InstanceSource instanceSource) {
        this.source = instanceSource;
    }

    public InstanceSource getSource() {
        return source;
    }

    @Override
    public boolean validate() {
        // 校验通用参数, 具体子类的参数交由子类校验
        if(name == null || name.isEmpty() || interval <= 0 || type == null){
            return false;
        }
        return validate0();
    }

    /**
     * 具体的初始化逻辑
     */
    protected abstract void init();

    /**
     * 子类参数校验
     */
    protected abstract boolean validate0();

    /**
     * 更新前检查是否需要更新
     * @return 无需更新返回假, 否则返回真
     */
    protected abstract boolean query();

    /**
     * 具体执行逻辑
     */
    protected abstract void run0();
}
