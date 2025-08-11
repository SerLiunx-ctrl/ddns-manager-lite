package com.serliunx.ddns.support;

import java.util.Collection;

/**
 * 断言
 *
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @version 1.0.0
 * @since 2024/5/15
 */
public final class Assert {

    private Assert(){throw new UnsupportedOperationException();}

    public static void notNull(Object object) {
        notNull(object, null);
    }

    public static void notNull(Object object, String msg) {
        if (object == null)
            throw new NullPointerException(msg);
    }

    public static void notNull(Object...objects) {
        for (Object object : objects)
            notNull(object);
    }

    public static void isPositive(int i) {
        if (i <= 0)
            throw new IllegalArgumentException("指定参数必须大于0!");
    }

    public static void isLargerThan(int source, int target) {
        if (source <= target)
            throw new IllegalArgumentException(String.format("%s太小了, 它必须大于%s", source, target));
    }

    public static void isLargerThan(long source, long target) {
        if (source <= target)
            throw new IllegalArgumentException(String.format("%s太小了, 它必须大于%s", source, target));
    }

    public static void notEmpty(Collection<?> collection) {
        notNull(collection);
        if (collection.isEmpty())
            throw new IllegalArgumentException("参数不能为空!");
    }

    public static void notEmpty(CharSequence charSequence) {
        notNull(charSequence);
        if (charSequence.length() == 0)
            throw new IllegalArgumentException("参数不能为空!");
    }
}
