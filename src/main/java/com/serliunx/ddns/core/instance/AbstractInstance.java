package com.serliunx.ddns.core.instance;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.serliunx.ddns.constant.InstanceSource;
import com.serliunx.ddns.constant.InstanceType;
import com.serliunx.ddns.support.NetworkContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.serliunx.ddns.constant.SystemConstants.XML_ROOT_INSTANCE_NAME;

/**
 * 实例抽象实现
 * @author SerLiunx
 * @since 1.0
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JacksonXmlRootElement(localName = XML_ROOT_INSTANCE_NAME)
public abstract class AbstractInstance implements Instance {

    private static final Logger log = LoggerFactory.getLogger(AbstractInstance.class);
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

    /**
     * 暂停状态
     * <li> 默认为非暂停状态
     */
    protected volatile boolean pause = false;

    @Override
    public void refresh() {
        // 调用子类的初始化逻辑
        init();
    }

    @Override
    public void run() {
        if (isPause()) // 暂停态检查, 已暂停则不继续进行.
            return;
        value = query();
        final String ipAddress = NetworkContextHolder.getIpAddress();
        try {
            if (value != null && !value.isEmpty()
                    && ipAddress != null && !ipAddress.isEmpty()) {
                if (value.equals(ipAddress))
                    return;
            }
            value = ipAddress;
            run0();
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            this.value = null;
        }
    }

    @Override
    public boolean validate() {
        // 校验通用参数, 具体子类的参数交由子类校验
        if(name == null || name.isEmpty() || interval <= 0 || type == null){
            return false;
        }
        return validate0();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getFatherName() {
        return fatherName;
    }

    @Override
    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    @Override
    public Long getInterval() {
        return interval;
    }

    @Override
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

    @Override
    public InstanceSource getSource() {
        return source;
    }

    @Override
    public void setPause(boolean pause) {
        this.pause = pause;
    }

    @Override
    public boolean isPause() {
        return pause;
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
     * 获取解析当前的ip地址
     * <li> 由子类完成具体逻辑
     * @return 返回当前解析记录的ip地址, 由子类决定.
     * @see AliyunInstance
     * @see TencentInstance
     * @see HuaweiInstance
     */
    protected abstract String query();

    /**
     * 具体执行逻辑
     */
    protected abstract void run0();
}
