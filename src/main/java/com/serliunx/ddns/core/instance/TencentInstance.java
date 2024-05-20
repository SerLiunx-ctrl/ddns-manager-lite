package com.serliunx.ddns.core.instance;

/**
 * 腾讯云实例定义
 * @author SerLiunx
 * @since 1.0
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
