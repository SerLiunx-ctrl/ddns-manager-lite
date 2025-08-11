package com.serliunx.ddns.config;

import com.serliunx.ddns.core.Combination;
import com.serliunx.ddns.support.Assert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 从启动命令中读取的配置信息
 *
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @version 1.0.3
 * @since 2024/12/24
 */
public final class CommandLineConfiguration extends AbstractConfiguration implements Combination<Configuration> {

    /**
     * 原始参数
     */
    private final String[] sourceArgs;
    /**
     * 待合并的其他配置信息
     */
    private final Collection<Configuration> configurations;

    /**
     * 配置项标记
     */
    private static final String TAG = "-D";
    /**
     * 配置赋值符号(=)
     */
    private static final String EQUAL = "=";
    /**
     * 配置缓存
     */
    private final Map<String, String> cache = new HashMap<>();

    public CommandLineConfiguration(String[] sourceArgs) {
        this(sourceArgs, new ArrayList<>());
    }

    public CommandLineConfiguration(String[] sourceArgs, Collection<Configuration> configurations) {
        this.sourceArgs = sourceArgs;
        this.configurations = configurations;
    }

    /**
     * 获取原始启动参数
     *
     * @return 原始启动参数
     */
    public String[] getSourceArgs() {
        return sourceArgs;
    }

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public void from(Configuration configuration) {
        configurations.add(configuration);
    }

    @Override
    public void from(Collection<? extends Configuration> configurations) {
        this.configurations.addAll(configurations);
    }

    @Override
    public Configuration getOriginal() {
        return this;
    }

    @Override
    public Collection<? extends Configuration> getCombinations() {
        return configurations;
    }

    @Override
    protected void refresh0() {
        if (sourceArgs == null)
            return;

        for (String arg : sourceArgs) {
            if (!arg.startsWith(TAG))
                continue;
            String key = arg.substring(TAG.length(), arg.indexOf(EQUAL));
            String value = arg.substring(arg.indexOf(EQUAL) + 1);
            cache.put(key, value);
        }

        // 载入配置
        load();
    }

    @Override
    protected void load0() {
        // 合并
        merge();
        // 更新
        valueMap.putAll(cache);
        // 清除缓存
        cache.clear();
    }

    /**
     * 将其他配置信息合并到当前配置信息
     * <li> 命令行参数的配置信息优先级高于其他配置信息
     * <li> 仅在持有锁的情况下访问
     */
    private void merge() {
        Assert.notEmpty(configurations);

        for (Configuration configuration : configurations) {
            if (configuration instanceof AbstractConfiguration) {
                AbstractConfiguration ac = (AbstractConfiguration) configuration;
                ac.refresh0();
            }

            final Map<String, String> keyValue = configuration.getAllKeyAndValue();
            keyValue.forEach((k, v) -> {
                if (!cache.containsKey(k))
                    cache.put(k, v);
            });
        }
    }
}
