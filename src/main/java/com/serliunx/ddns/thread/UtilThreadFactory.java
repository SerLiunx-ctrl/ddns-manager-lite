package com.serliunx.ddns.thread;

/**
 * 同 {@link TaskThreadFactory}, 暂未使用.
 *
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @version 1.0.0
 * @since 2024/5/15
 */
public class UtilThreadFactory extends TaskThreadFactory {

    @Override
    protected String getNamePattern() {
        return "ddns-util-%s";
    }
}
