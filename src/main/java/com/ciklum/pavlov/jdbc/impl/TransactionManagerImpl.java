package com.ciklum.pavlov.jdbc.impl;


import com.ciklum.pavlov.jdbc.TransactionManager;
import com.ciklum.pavlov.util.ThreadLocalConnection;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.lang3.StringUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.Objects;
import java.util.function.Supplier;

import static com.ciklum.pavlov.jdbc.handler.JDBCUtil.EXCEPTION_SECTION;

@Slf4j
public class TransactionManagerImpl implements TransactionManager {
    public static final String START_SAVE_POINT = "START";
    private BasicDataSource dataSource;
    private Savepoint startSavePoint;

    public TransactionManagerImpl(BasicDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void start() {
        if (ThreadLocalConnection.getConnection() != null) {
            throw new IllegalStateException("Thread local already has connection");
        }
        Connection connection;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(Boolean.FALSE);
            startSavePoint = connection.setSavepoint(START_SAVE_POINT);
        } catch (SQLException ex) {
            log.error("Sql exception has been happened in start method of transaction manager");
            throw new IllegalStateException("Sql exception has been happened during start of transaction: " + ex.getMessage());
        }
        ThreadLocalConnection.setConnection(connection);
    }

    public void end() {
        Connection connection = ThreadLocalConnection.getConnection();
        if (Objects.isNull(connection)) {
            log.error("Application exception was thrown due to attempt of appeal to empty thread local !");
            throw new IllegalStateException("ThreadLocal is empty");
        }
        try {
            String exception = connection.getClientInfo(EXCEPTION_SECTION);
            if (!StringUtils.isBlank(exception)) {
                connection.rollback(startSavePoint);
            } else {
                connection.commit();
            }
            connection.close();
            ThreadLocalConnection.removeConnection();
        } catch (SQLException ex) {
            log.error("Exception has been happened during end of transaction!");
            throw new IllegalStateException("Exception has been happened during end of transaction! Transaction canceled!" + ex.getMessage());
        }
    }

    @Override
    public <T> T executeInTransaction(Supplier<T> supplier) {
        T result;
        start();
        try {
            result = supplier.get();
        } finally {
            end();
        }
        return result;
    }

}