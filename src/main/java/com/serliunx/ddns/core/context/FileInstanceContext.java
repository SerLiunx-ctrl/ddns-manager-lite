package com.serliunx.ddns.core.context;

import com.serliunx.ddns.constant.SystemConstants;
import com.serliunx.ddns.core.factory.JsonFileInstanceFactory;
import com.serliunx.ddns.core.factory.XmlFileInstanceFactory;
import com.serliunx.ddns.core.factory.YamlFileInstanceFactory;

/**
 * 文件形式的实例容器
 *
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @version 1.0.0
 * @since 2024/5/15
 */
public class FileInstanceContext extends AbstractInstanceContext {

    public FileInstanceContext() {
        addListableInstanceFactory(new JsonFileInstanceFactory(SystemConstants.USER_INSTANCE_DIR));
        addListableInstanceFactory(new XmlFileInstanceFactory(SystemConstants.USER_INSTANCE_DIR));
        addListableInstanceFactory(new YamlFileInstanceFactory(SystemConstants.USER_INSTANCE_DIR));
        // 刷新容器
        refresh();
    }

    @Override
    protected void clear0() {
        clearCache();
    }
}
