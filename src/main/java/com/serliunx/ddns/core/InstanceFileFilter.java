package com.serliunx.ddns.core;

import java.io.File;
import java.io.FileFilter;

/**
 * 文件过滤器, 用于加载过滤存储在文件中的实例信息时
 *
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @version 1.0.0
 * @since 2024/5/15
 * @see com.serliunx.ddns.core.factory.FileInstanceFactory
 */
public final class InstanceFileFilter implements FileFilter {

    private final String[] fileSuffix;

    public InstanceFileFilter(String[] fileSuffix) {
        this.fileSuffix = fileSuffix;
    }

    @Override
    public boolean accept(File pathname) {
        if (!pathname.isFile())
            return false;
        for (String suffix : fileSuffix) {
            if (pathname.getName().endsWith(suffix)) {
                return true;
            }
        }
        return false;
    }
}
