package com.serliunx.ddns.core.context;

import com.serliunx.ddns.constant.InstanceType;
import com.serliunx.ddns.core.Clearable;
import com.serliunx.ddns.core.factory.ListableInstanceFactory;
import com.serliunx.ddns.core.instance.Instance;
import com.serliunx.ddns.exception.InstanceExistsException;
import com.serliunx.ddns.support.Assert;
import com.serliunx.ddns.util.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import static com.serliunx.ddns.util.InstanceUtils.validateInstance;

/**
 * 实例容器的抽象实现, 定义大部分公共逻辑
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @version 1.0.0
 * @since 2024/5/15
 */
public abstract class AbstractInstanceContext implements InstanceContext, MultipleSourceInstanceContext {

    private static final Logger log = LoggerFactory.getLogger(AbstractInstanceContext.class);

    private final Set<ListableInstanceFactory> listableInstanceFactories = new HashSet<>();

    /**
     * 实例操作锁
     * <li> 为保证数据的一致性, 实例新增、删除等相关操作尽量加锁
     */
    private final Lock instanceLock = new ReentrantLock();

    /**
     * 完整的实例信息
     * <li> 作为主要操作对象
     */
    private Map<String, Instance> instanceMap = new HashMap<>(16);

    /**
     * 实例信息缓存, 此时的实例继承关系并不完整
     * <li> 不能作为主要的操作对象
     * <li> 容器一般会在刷新完毕后清空该Map, 具体取决于容器本身
     */
    private Map<String, Instance> cacheInstanceMap = new HashMap<>(16);

    @Override
    public void refresh() {
        try {
            instanceLock.lock();
            if (listableInstanceFactories.isEmpty())
                return;

            // 初始化所有实例工厂
            listableInstanceFactories.stream()
                    .filter(f -> f != this)
                    .forEach(ListableInstanceFactory::refresh);
            // 加载、过滤所有实例
            Set<Instance> instances = new HashSet<>();

            // 高优先级的实例工厂会覆盖低优先级实例工厂所加载的实例
            listableInstanceFactories.stream()
                    .sorted()
                    .forEach(f -> instances.addAll(f.getInstances()));

            // 初次载入
            cacheInstanceMap = new HashMap<>(instances.stream().collect(Collectors.toMap(Instance::getName, i -> i)));
            Set<Instance> builtInstances = buildInstances(instances);

            instanceMap = builtInstances.stream().collect(Collectors.toMap(Instance::getName, i -> i));
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            instanceLock.unlock();
        }
    }

    @Override
    public void addInstance(Instance instance) {
        validateInstance(instance);
        try {
            instanceLock.lock();
            String name = instance.getName();
            Instance instanceExists = instanceMap.get(name);
            if (instanceExists != null)
                throw new InstanceExistsException("该实例已存在!", name, instanceExists);
            else
                instanceMap.put(name, instance);
        }catch (Exception e){
            throw new RuntimeException(e);
        }finally {
            instanceLock.unlock();
        }
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

    @Override
    public void clear() {
        if(isClearable())
            clear0();
    }

    /**
     * 子类清理逻辑
     */
    protected abstract void clear0();

    /**
     * 缓存清理
     */
    protected void clearCache() {
        if (cacheInstanceMap != null
                && !cacheInstanceMap.isEmpty()){
            int size = cacheInstanceMap.size();
            cacheInstanceMap.clear();
            // 清理实例工厂的缓存信息
            listableInstanceFactories.forEach(Clearable::clear);
            listableInstanceFactories.clear();
            log.info("共清理缓存信息 => {} 条", size);
        }
    }

    /**
     * 构建完整的实例信息
     * @param instances 实例信息
     * @return 属性设置完整的实例
     */
    private Set<Instance> buildInstances(Collection<Instance> instances) {
        //设置实例信息, 如果需要从父类继承
        return instances.stream()
                .filter(i -> !InstanceType.INHERITED.equals(i.getType()))
                .peek(i -> {
                    String fatherName = i.getFatherName();
                    if (fatherName != null && !fatherName.isEmpty()) {
                        Instance fatherInstance = cacheInstanceMap.get(fatherName);
                        if (fatherInstance != null) {
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
