package com.serliunx.ddns.support;

import java.lang.management.ManagementFactory;

/**
 * @author SerLiunx
 * @since 1.0
 */
public final class SystemSupport {

    private static final String PID;

    static {
        PID = ManagementFactory.getRuntimeMXBean().getName().split("@")[0];
    }

    private SystemSupport(){throw new UnsupportedOperationException();}

    public static String getPid() {
        return PID;
    }
}
