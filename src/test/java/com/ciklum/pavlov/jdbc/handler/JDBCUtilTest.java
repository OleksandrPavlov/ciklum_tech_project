package com.ciklum.pavlov.jdbc.handler;

import com.ciklum.pavlov.exceptions.ApplicationException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.Connection;
import java.sql.SQLClientInfoException;

import static com.ciklum.pavlov.jdbc.handler.JDBCUtil.EXCEPTION_SECTION;
import static com.ciklum.pavlov.jdbc.handler.JDBCUtil.putErrorMsgToConnection;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class JDBCUtilTest {
    @Mock
    private Connection connection;


    @Test
    public void Should_PutErrorMsgToConnection_When_methodCalled() throws SQLClientInfoException {
        String msg = "exceptionMsg";
        putErrorMsgToConnection(msg, connection);
        verify(connection).setClientInfo(EXCEPTION_SECTION, msg);
    }

    @Test(expected = ApplicationException.class)
    public void Should_ThrowApplicationException_When_SqlExceptionDuringWritingClientInfo() throws SQLClientInfoException {
        String msg = "exceptionMsg";
        doThrow(SQLClientInfoException.class).when(connection).setClientInfo(EXCEPTION_SECTION, msg);
        putErrorMsgToConnection(msg, connection);
    }
}