package com.ciklum.pavlov.commands.impl.account;

import com.ciklum.pavlov.context.ApplicationContext;
import com.ciklum.pavlov.models.User;
import com.ciklum.pavlov.services.AccountService;
import com.ciklum.pavlov.util.io.CustomReader;
import com.ciklum.pavlov.util.io.CustomWriter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;
import java.util.ResourceBundle;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doReturn;

@RunWith(MockitoJUnitRunner.class)
public class SignInCommandTest {
    public static final String VALID_USER_NAME = "oleksandr";
    public static final String VALID_PASSWORD = "Oleksandr1@";

    @Mock
    private AccountService accountService;
    @Mock
    private ApplicationContext context;
    @Mock
    private SignInCommand signInCommand;
    @Mock
    private CustomWriter customWriter;
    @Mock
    private CustomReader customReader;
    @Spy
    private final ResourceBundle resourceBundle = ResourceBundle.getBundle("application");

    @Before
    public void before() {
        when(context.getWriter()).thenReturn(customWriter);
        when(context.getReader()).thenReturn(customReader);
        when(context.getResourceBundle()).thenReturn(resourceBundle);
        signInCommand = new SignInCommand(context, accountService);
    }

    @Test
    public void shouldAuthorizeUserWhenExecuteMethodCalled() {
        when(customReader.readLine()).thenReturn(VALID_USER_NAME).thenReturn(VALID_PASSWORD);
        User user = new User(VALID_USER_NAME, VALID_PASSWORD);
        when(accountService.signUser(user)).thenReturn(Optional.of(user));

        boolean executingStatus = signInCommand.execute();

        verify(accountService).signUser(user);
        verify(context).setCurrentUser(any(User.class));
        assertTrue(executingStatus);
    }
}