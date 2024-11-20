package com.serliunx.ddns.core.factory;

import com.serliunx.ddns.core.instance.Instance;
import com.serliunx.ddns.support.Assert;
import com.serliunx.ddns.support.sqlite.SQLiteConnector;

import java.util.Collections;
import java.util.Set;

/**
 * 数据库示例工厂: 从数据库中(sqlite)存储、加载示例信息
 *
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @version 1.0.3
 * @since 2024/11/20
 */
public final class DatabaseInstanceFactory extends AbstractInstanceFactory implements PersistentInstanceFactory {

    private final SQLiteConnector connector;

    public DatabaseInstanceFactory(SQLiteConnector connector) {
        this.connector = connector;
    }

    @Override
    protected Set<Instance> load() {
        Assert.notNull(connector, "数据库连接不能为空!");
        connector.refresh();
        return Collections.emptySet();
    }

    @Override
    public boolean save(Instance instance) {
        return false;
    }
}
