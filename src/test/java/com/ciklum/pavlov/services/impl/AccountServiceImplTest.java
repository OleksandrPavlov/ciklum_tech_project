package com.ciklum.pavlov.services.impl;

import com.ciklum.pavlov.dao.UserDao;
import com.ciklum.pavlov.jdbc.TransactionManager;
import com.ciklum.pavlov.models.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;
import java.util.function.Supplier;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AccountServiceImplTest {
    public static final String PASSWORD = "password";
    public static final String LOGIN = "login";
    public static final String ANOTHER_LOGIN = "anotherLogin";
    public static final String ANOTHER_PASSWORD = "anotherPassword";

    @Mock
    private UserDao userDao;
    @Mock
    private TransactionManager transactionManager;
    @InjectMocks
    private AccountServiceImpl accountService;
    @Captor
    private ArgumentCaptor<Supplier<Optional<User>>> userCaptor;
    @Captor
    private ArgumentCaptor<Supplier<Long>> longCapture;


    @Test
    public void Should_ReturnUser_When_ValidPassword() {
        User passedUser = new User();
        passedUser.setLogin(LOGIN);
        passedUser.setPassword(PASSWORD);
        User retrievedUser = new User();
        retrievedUser.setLogin(LOGIN);
        retrievedUser.setPassword(PASSWORD);
        when(transactionManager.executeInTransaction(any(Supplier.class))).thenReturn(Optional.of(retrievedUser));
        accountService.signUser(passedUser);
        verify(transactionManager).executeInTransaction(userCaptor.capture());
        userCaptor.getValue().get();
        verify(userDao).findByLogin(LOGIN);
    }

    @Test
    public void Should_ReturnEmptyUser_When_InvalidPassword() {
        User passedUser = new User();
        passedUser.setLogin(LOGIN);
        passedUser.setPassword(PASSWORD);
        User retrievedUser = new User();
        retrievedUser.setLogin(ANOTHER_LOGIN);
        retrievedUser.setPassword(ANOTHER_PASSWORD);
        when(transactionManager.executeInTransaction(any(Supplier.class))).thenReturn(Optional.of(retrievedUser));
        Optional<User> optionalUser = accountService.signUser(passedUser);
        verify(transactionManager).executeInTransaction(userCaptor.capture());
        userCaptor.getValue().get();
        verify(userDao).findByLogin(LOGIN);
        assertFalse(optionalUser.isPresent());
    }

    @Test
    public void Should_ReturnValidUserId_When_RegisterUserCalled() {
        User passedUser = new User();
        passedUser.setLogin(LOGIN);
        passedUser.setPassword(PASSWORD);
        when(transactionManager.executeInTransaction(any(Supplier.class))).thenReturn(1l);
        accountService.registerUser(passedUser);
        verify(transactionManager).executeInTransaction(longCapture.capture());
        longCapture.getValue().get();
        verify(userDao).add(passedUser);
    }

}