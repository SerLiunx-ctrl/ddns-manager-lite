package com.serliunx.ddns.core.factory;

import com.serliunx.ddns.core.InstanceFileFilter;
import com.serliunx.ddns.core.instance.Instance;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author SerLiunx
 * @since 1.0
 */
public abstract class FileInstanceFactory extends AbstractInstanceFactory {

    /**
     * 存储实例信息的文件夹路径
     */
    protected String instanceDir;

    public FileInstanceFactory(String instanceDir) {
        this.instanceDir = instanceDir;
    }

    @Override
    protected Set<Instance> load() {
        Set<File> files = loadFiles();
        if(files != null && !files.isEmpty()){
            return files.stream()
                    .map(this::loadInstance)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toCollection(HashSet::new));
        }
        return Collections.emptySet();
    }

    @Override
    public int getPriority() {
        return 256;
    }

    /**
     * 交由具体的子类去加载实例, 比如: json格式的实例信息、xml格式的实例信息
     * @param file 文件信息
     * @return 实例
     */
    protected abstract Instance loadInstance(File file);

    /**
     * 子类要设置自己可以加载的文件后缀名
     * <li> 后缀名仅仅是一个标记符, 文件不一定要有后缀名哦
     * @return 文件后缀名
     */
    protected abstract String[] fileSuffix();

    /**
     * 载入目录下所有符合条件的文件
     */
    private Set<File> loadFiles(){
        File pathFile = new File(instanceDir);
        if(!pathFile.exists()){
            boolean result = pathFile.mkdirs();
            if(!result){
                throw new IllegalArgumentException("create path failed");
            }
        }
        if(!pathFile.isDirectory()){
            throw new IllegalArgumentException("path is not a directory");
        }
        File[] files = pathFile.listFiles(new InstanceFileFilter(fileSuffix()));
        if(files == null || files.length == 0){
            return Collections.emptySet();
        }
        return Arrays.stream(files).collect(Collectors.toCollection(HashSet::new));
    }
}
