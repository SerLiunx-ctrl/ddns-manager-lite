package com.serliunx.ddns.thread;

/**
 * @author SerLiunx
 * @since 1.0
 */
public class UtilThreadFactory extends TaskThreadFactory {

    @Override
    protected String getNamePattern() {
        return "ddns-util-%s";
    }
}
