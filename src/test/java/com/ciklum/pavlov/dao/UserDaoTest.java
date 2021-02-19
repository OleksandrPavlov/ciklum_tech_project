package com.ciklum.pavlov.dao;

import com.ciklum.pavlov.dao.impl.UserDaoImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.sql.Connection;
import java.util.Properties;

public class UserDaoTest {

    private UserDao userDao;
    @Mock
    private Properties sqlProperties;
    @Mock
    Connection connection;

    @Before
    public void init() {
        userDao = new UserDaoImpl(sqlProperties);
    }

    @Test
    public void Should_InsertUserToDb() {

    }
}