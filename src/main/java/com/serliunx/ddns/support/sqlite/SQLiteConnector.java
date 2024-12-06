package com.serliunx.ddns.support.sqlite;

import com.serliunx.ddns.constant.SystemConstants;
import com.serliunx.ddns.core.Refreshable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * SQLite 数据库连接
 *
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @version 1.0.3
 * @since 2024/11/20
 */
public final class SQLiteConnector implements Refreshable {

    private volatile Connection connection;
    private volatile boolean initialized = false;

    private final Lock initLock = new ReentrantLock();

    private static final Logger log = LoggerFactory.getLogger(SQLiteConnector.class);
    private static final SQLiteConnector INSTANCE = new SQLiteConnector();

    // private-ctor
    private SQLiteConnector() {}

    @Override
    public void refresh() {
        init();
    }

    /**
     * 连接初始化
     */
    private void init() {
        if (initialized) {
            log.warn("sql connection already initialized");
            return;
        }

        if (!initLock.tryLock()) {
            log.error("sql connection already initialing");
        }

        try {
            log.info("initialing sqlite connection.");
            connection = DriverManager.getConnection(SystemConstants.SQLITE_URL);

            // 尝试创建数据库表, 只会执行一次
            tryCreateTables();

            initialized = true;
            log.info("sqlite connection successfully initialized.");
        } catch (Exception e) {
            initialized = false;
            log.error("sql connection initialization exception: ", e);
        } finally {
            initLock.unlock();
        }
    }

    /**
     * 尝试创建数据库表
     * <li> 不存在时创建
     */
    private void tryCreateTables() {
        if (connection == null) {
            throw new IllegalStateException("sql connection not initialized");
        }
    }

    /**
     * 是否已经初始化
     */
    public boolean isInitialized() {
        return initialized;
    }

    public static SQLiteConnector getInstance() {
        return INSTANCE;
    }
}
