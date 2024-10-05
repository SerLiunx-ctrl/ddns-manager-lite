package com.serliunx.ddns.core.instance;

/**
 * 腾讯云实例定义
 *
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @version 1.0.0
 * @since 2024/5/15
 */
public class TencentInstance extends AbstractInstance {

    @Override
    protected void init() {

    }

    @Override
    protected void run0() {

    }

    @Override
    protected String query() {
        return null;
    }

    @Override
    protected boolean validate0() {
        return false;
    }

    @Override
    public String toString() {
        return "TencentInstance{" +
                "source=" + source +
                ", type=" + type +
                ", fatherName='" + fatherName + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
