package com.ciklum.pavlov.dao.impl;

import com.ciklum.pavlov.dao.UserDao;
import com.ciklum.pavlov.models.User;
import com.ciklum.pavlov.util.ThreadLocalConnection;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Properties;

import static com.ciklum.pavlov.constants.SQLQueriesConstants.INSERT_INTO_USERS;
import static com.ciklum.pavlov.constants.SQLQueriesConstants.SELECT_USER_BY_LOGIN;
import static com.ciklum.pavlov.jdbc.handler.HandlerFactory.USER_RESULT_SET_HANDLER;
import static com.ciklum.pavlov.jdbc.handler.JDBCUtil.putErrorMsgToConnection;

@Slf4j
public class UserDaoImpl implements UserDao {

    private final Properties queryProperties;
    private final QueryRunner queryRunner;

    public UserDaoImpl(Properties queryProperties) {
        this.queryProperties = queryProperties;
        queryRunner = new QueryRunner();
    }


    @Override
    public long add(User user) {
        Connection connection = ThreadLocalConnection.getConnection();
        try {
            return queryRunner.insert(connection, queryProperties.getProperty(INSERT_INTO_USERS), new ScalarHandler<BigInteger>(),
                    user.getLogin(),
                    user.getPassword()
            ).longValue();
        } catch (SQLException ex) {
            putErrorMsgToConnection(ex.getMessage(), connection);
        }
        return -1;
    }

    @Override
    public Optional<User> findByLogin(String login) {
        Connection connection = ThreadLocalConnection.getConnection();
        try {
            return queryRunner.query(connection, queryProperties.getProperty(SELECT_USER_BY_LOGIN),
                    USER_RESULT_SET_HANDLER, login);
        } catch (SQLException e) {
            putErrorMsgToConnection(e.getMessage(), connection);
        }
        return Optional.empty();
    }
}
