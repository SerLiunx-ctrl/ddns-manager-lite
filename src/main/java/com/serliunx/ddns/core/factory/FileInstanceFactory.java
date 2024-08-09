package com.serliunx.ddns.core.factory;

import com.serliunx.ddns.core.FileAttachment;
import com.serliunx.ddns.core.InstanceFileFilter;
import com.serliunx.ddns.core.instance.Instance;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 文件相关实例工厂, 定义所有来源为文件的实例工厂通用逻辑
 *
 * @see JacksonFileInstanceFactory 使用Jackson序列化、反序列化的实例
 * @see YamlFileInstanceFactory 使用SankeYaml序列化、反序列化的实例
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @version 1.0.0
 * @since 2024/5/15
 */
public abstract class FileInstanceFactory extends AbstractInstanceFactory implements FileAttachment {

    /**
     * 存储实例信息的文件夹路径
     */
    protected String instanceDir;
    protected Set<File> filesAttachments;

    public FileInstanceFactory(String instanceDir) {
        this.instanceDir = instanceDir;
    }

    @Override
    public boolean exists(File file) {
        return filesAttachments.contains(file);
    }

    @Override
    public boolean detach(File file) {
        if (!exists(file))
            return false;
        return filesAttachments.remove(file);
    }

    @Override
    public void attach(File file) {
        filesAttachments.add(file);
    }

    @Override
    public Collection<File> getAttachments() {
        return filesAttachments;
    }

    @Override
    public int getPriority() {
        return 256;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(instanceDir: " + instanceDir + ", priority: " + getPriority() + ")";
    }

    @Override
    protected Set<Instance> load() {
        Set<File> files = loadFiles();

        filesAttachments = files;

        if (files != null && !files.isEmpty()) {
            return files.stream()
                    .map(this::loadInstance)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toCollection(HashSet::new));
        }
        return Collections.emptySet();
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
    private Set<File> loadFiles() {
        File pathFile = new File(instanceDir);
        if (!pathFile.exists()) {
            boolean result = pathFile.mkdirs();
            if (!result) {
                throw new IllegalArgumentException("create path failed");
            }
        }
        if (!pathFile.isDirectory()) {
            throw new IllegalArgumentException("path is not a directory");
        }
        File[] files = pathFile.listFiles(new InstanceFileFilter(fileSuffix()));
        if (files == null || files.length == 0) {
            return Collections.emptySet();
        }
        return Arrays.stream(files).collect(Collectors.toCollection(HashSet::new));
    }
}
