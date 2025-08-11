package com.serliunx.ddns.core.factory;

import com.serliunx.ddns.constant.InstanceType;
import com.serliunx.ddns.core.instance.Instance;
import com.serliunx.ddns.support.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

import static com.serliunx.ddns.util.InstanceUtils.validateInstance;

/**
 * 实例工厂抽象实现, 定义通用逻辑及实例存储.
 *
 * @see FileInstanceFactory
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @version 1.0.0
 * @since 2024/5/15
 */
public abstract class AbstractInstanceFactory implements InstanceFactory, ListableInstanceFactory {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * 实例信息
     */
    protected Map<String, Instance> instanceMap;

    @Override
    public Instance getInstance(String instanceName) {
        Assert.notNull(instanceName);
        final Instance instance = instanceMap.get(instanceName);
        Assert.notNull(instance);
        return instance;
    }

    @Override
    public Set<Instance> getInstances() {
        return instanceMap == null ? Collections.emptySet() : new HashSet<>(instanceMap.values());
    }

    @Override
    public Map<String, Instance> getInstanceOfType(InstanceType type) {
        if (instanceMap == null)
            return Collections.emptyMap();

        return instanceMap.values()
                .stream()
                .filter(i -> i.getType().equals(type))
                .collect(Collectors.toMap(Instance::getName, i -> i));
    }

    @Override
    public void addInstance(Instance instance) {
        validateInstance(instance);
        instanceMap.put(instance.getName(), instance);
    }

    @Override
    public void refresh() {
        Set<Instance> instances = load();
        if (instances != null && !instances.isEmpty())
            instanceMap = new HashMap<>(instances.stream()
                    .collect(Collectors.toMap(Instance::getName, i -> i)));
    }

    @Override
    public int getPriority() {
        return Integer.MAX_VALUE;
    }

    @Override
    public final void clear() {
        if(isClearable() && instanceMap != null)
            clear0();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(priority: " + getPriority() + ")";
    }

    /**
     * 交由子类去加载实例信息
     * @return 实例信息
     */
    protected abstract Set<Instance> load();

    /**
     * 清理逻辑
     */
    protected void clear0(){
        final int size = instanceMap.size();
        instanceMap.clear();
        log.info("缓存信息清理 => {} 条", size);
    }
}
