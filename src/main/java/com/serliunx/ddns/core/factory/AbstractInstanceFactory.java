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
 * @author SerLiunx
 * @since 1.0
 */
public abstract class AbstractInstanceFactory implements InstanceFactory, ListableInstanceFactory {

    private static final Logger log = LoggerFactory.getLogger(AbstractInstanceFactory.class);

    /**
     * 实例信息
     */
    private Map<String, Instance> instanceMap;

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
        Assert.notNull(instanceMap);
        return instanceMap.values()
                .stream()
                .filter(i -> i.getType().equals(type))
                .collect(Collectors.toMap(Instance::getName, i -> i));
    }

    @Override
    public boolean addInstance(Instance instance, boolean override) {
        validateInstance(instance);
        Instance i = instanceMap.get(instance.getName());
        if(override && i != null){
            return false;
        }
        instanceMap.put(instance.getName(), instance);
        return true;
    }

    @Override
    public void addInstance(Instance instance) {
        addInstance(instance, false);
    }

    @Override
    public void refresh() {
        Set<Instance> instances = load();
        if(instances != null && !instances.isEmpty())
            instanceMap = new HashMap<>(instances.stream()
                    .collect(Collectors.toMap(Instance::getName, i -> i)));
    }

    @Override
    public int getPriority() {
        return Integer.MAX_VALUE;
    }

    @Override
    public void afterRefresh() {
        int size = instanceMap.size();
        instanceMap.clear();
        log.debug("缓存信息清理 => {} 条", size);
    }

    /**
     * 交由子类去加载实例信息
     * @return 实例信息
     */
    protected abstract Set<Instance> load();
}
