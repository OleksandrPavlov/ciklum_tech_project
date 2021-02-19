package com.ciklum.pavlov.jdbc.impl;

import com.ciklum.pavlov.util.ThreadLocalConnection;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.function.Supplier;

import static com.ciklum.pavlov.jdbc.handler.JDBCUtil.EXCEPTION_SECTION;
import static com.ciklum.pavlov.jdbc.impl.TransactionManagerImpl.START_SAVE_POINT;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TransactionManagerImplTest {
    @Mock
    private Connection connection;
    @Mock
    private BasicDataSource dataSource;
    @InjectMocks
    private TransactionManagerImpl transactionManager;

    @Before
    public void init() {
        ThreadLocalConnection.removeConnection();
    }

    @Test
    public void Should_SetUpSavePointOnConnection_When_StartMethodCalled() throws SQLException {
        when(dataSource.getConnection()).thenReturn(connection);
        transactionManager.start();
        verify(connection).setSavepoint(START_SAVE_POINT);
    }

    @Test(expected = IllegalStateException.class)
    public void Should_ThrowISE_When_ConnectionAlreadyInThreadLocal() {
        ThreadLocalConnection.setConnection(connection);
        transactionManager.start();
    }

    @Test
    public void Should_SetAutoCommitModeOff_When_RightFlow() throws SQLException {
        when(dataSource.getConnection()).thenReturn(connection);
        transactionManager.start();
        verify(connection).setAutoCommit(Boolean.FALSE);
    }

    @Test(expected = IllegalStateException.class)
    public void Should_ThrowISE_When_DataSourceConnectionRetrievingError() throws SQLException {
        when(dataSource.getConnection()).thenThrow(SQLException.class);
        transactionManager.start();
    }

    @Test(expected = IllegalStateException.class)
    public void Should_ThrowISE_When_TurningOffAutoCommitModeError() throws SQLException {
        when(dataSource.getConnection()).thenReturn(connection);
        doThrow(SQLException.class).when(connection).setAutoCommit(Boolean.FALSE);
        transactionManager.start();
    }

    @Test(expected = IllegalStateException.class)
    public void Should_ThrowISE_When_AdjustingSavePointError() throws SQLException {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.setSavepoint(START_SAVE_POINT)).thenThrow(SQLException.class);
        transactionManager.start();
    }


    @Test(expected = IllegalStateException.class)
    public void Should_ThrowISE_When_NoConnectionInThreadLocal() {
        transactionManager.end();
    }

    @Test
    public void Should_RollBack_When_ClientInfoHasErrors() throws SQLException {
        String exceptionMsg = "exceptionMsg";
        when(connection.getClientInfo(EXCEPTION_SECTION)).thenReturn(exceptionMsg);
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.setSavepoint(START_SAVE_POINT)).thenReturn(mock(Savepoint.class));
        transactionManager.start();
        transactionManager.end();
        verify(connection).rollback(any(Savepoint.class));
    }

    @Test
    public void Should_CommitChanges_When_NoErrorsInClientInfo() throws SQLException {
        ThreadLocalConnection.setConnection(connection);
        when(connection.getClientInfo(EXCEPTION_SECTION)).thenReturn(null);
        transactionManager.end();
        verify(connection).commit();
    }


    @Test
    public void Should_CloseConnection_When_NormalFlow() throws SQLException {
        ThreadLocalConnection.setConnection(connection);
        when(connection.getClientInfo(EXCEPTION_SECTION)).thenReturn(null);
        transactionManager.end();
        verify(connection).close();
    }

    @Test(expected = IllegalStateException.class)
    public void Should_ThrowISE_When_ExceptionDuringRollingBack() throws SQLException {
        String exceptionMsg = "exceptionMsg";
        when(connection.getClientInfo(EXCEPTION_SECTION)).thenReturn(exceptionMsg);
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.setSavepoint(START_SAVE_POINT)).thenReturn(mock(Savepoint.class));
        doThrow(SQLException.class).when(connection).rollback(any(Savepoint.class));
        transactionManager.start();
        transactionManager.end();
    }

    @Test
    public void Should_ExecuteInTransaction_When_ExecuteInTransactionMethodCalled() throws SQLException {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.getClientInfo(EXCEPTION_SECTION)).thenReturn(StringUtils.EMPTY);
        Supplier<Integer> supplier = (Supplier<Integer>) Mockito.mock(Supplier.class);
        transactionManager.executeInTransaction(supplier);
        verify(supplier).get();
    }
}