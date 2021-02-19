package com.ciklum.pavlov.jdbc.handler;

import com.ciklum.pavlov.exceptions.ApplicationException;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.SQLClientInfoException;

@Slf4j
public class JDBCUtil {
    public static final String EXCEPTION_SECTION = "exceptionSection";
    public static final String CLIENT_INFO_ERROR_MESSAGE = "ClientInfoException has been happened during attempt of writing exception notification";

    private JDBCUtil() {

    }

    public static void putErrorMsgToConnection(String msg, Connection connection) {
        try {
            connection.setClientInfo(EXCEPTION_SECTION, msg);
        } catch (SQLClientInfoException e) {
            log.error(CLIENT_INFO_ERROR_MESSAGE);
            throw new ApplicationException(e.getMessage());
        }
        log.error(msg);
    }
}
