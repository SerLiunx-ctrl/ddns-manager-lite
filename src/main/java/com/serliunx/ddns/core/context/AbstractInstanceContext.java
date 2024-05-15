package com.serliunx.ddns.core.context;

import com.serliunx.ddns.constant.InstanceType;
import com.serliunx.ddns.core.factory.ListableInstanceFactory;
import com.serliunx.ddns.core.instance.Instance;
import com.serliunx.ddns.support.Assert;
import com.serliunx.ddns.support.Refreshable;
import com.serliunx.ddns.util.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

import static com.serliunx.ddns.util.InstanceUtils.validateInstance;

/**
 * 实例容器的抽象实现, 定义大部分公共逻辑
 * @author SerLiunx
 * @since 1.0
 */
public abstract class AbstractInstanceContext implements InstanceContext, MultipleSourceInstanceContext {

    private static final Logger log = LoggerFactory.getLogger(AbstractInstanceContext.class);
    private final Set<ListableInstanceFactory> listableInstanceFactories = new HashSet<>();

    /**
     * 完整的实例信息
     * <li> 作为主要操作对象
     */
    private Map<String, Instance> instanceMap;

    /**
     * 实例信息缓存, 此时的实例继承关系并不完整
     * <li> 不能作为主要的操作对象
     * <li> 容器一般会在刷新完毕后清空该Map, 具体取决于容器本身
     */
    private Map<String, Instance> cacheInstanceMap;

    @Override
    public void refresh() {
        if(listableInstanceFactories.isEmpty())
            return;

        // 初始化所有实例工厂
        listableInstanceFactories.stream()
                .filter(f -> f != this)
                .forEach(ListableInstanceFactory::refresh);
        // 加载、过滤所有实例
        Set<Instance> instances = new HashSet<>();
        listableInstanceFactories.forEach(f -> instances.addAll(f.getInstances()));

        // TODO 加载实例, 按照实例工厂的优先级从低到高优先级排, 高优先级的实例会覆盖低优先级的实例信息(如果存在重复的实例信息)

        // 初次载入
        cacheInstanceMap = new HashMap<>(instances.stream().collect(Collectors.toMap(Instance::getName, i -> i)));
        Set<Instance> builtInstances = buildInstances(instances);

        instanceMap = builtInstances.stream().collect(Collectors.toMap(Instance::getName, i -> i));

        // 调用善后处理钩子函数
        afterRefresh();
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
    public void addListableInstanceFactory(ListableInstanceFactory listableInstanceFactory) {
        listableInstanceFactories.add(listableInstanceFactory);
    }

    @Override
    public Set<ListableInstanceFactory> getListableInstanceFactories() {
        return listableInstanceFactories;
    }

    /**
     * 善后工作
     */
    public abstract void afterRefresh();

    /**
     * 缓存清理
     */
    protected void clearCache(){
        int size = cacheInstanceMap.size();
        cacheInstanceMap.clear();
        log.debug("缓存信息清理 => {} 条", size);
        // 清理实例工厂的缓存信息
        listableInstanceFactories.forEach(Refreshable::afterRefresh);
    }

    /**
     * 构建完整的实例信息
     * @param instances 实例信息
     * @return 属性设置完整的实例
     */
    private Set<Instance> buildInstances(Collection<Instance> instances){
        //设置实例信息, 如果需要从父类继承
        return instances.stream()
                .filter(i -> !InstanceType.INHERITED.equals(i.getType()))
                .peek(i -> {
                    String fatherName = i.getFatherName();
                    if(fatherName != null && !fatherName.isEmpty()){
                        Instance fatherInstance = cacheInstanceMap.get(fatherName);
                        if(fatherInstance != null){
                            try {
                                ReflectionUtils.copyField(fatherInstance, i, true);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                })
                .collect(Collectors.toCollection(HashSet::new));
    }
}
