package com.ciklum.pavlov.commands.impl.account;

import com.ciklum.pavlov.context.ApplicationContext;
import com.ciklum.pavlov.models.User;
import com.ciklum.pavlov.services.AccountService;
import com.ciklum.pavlov.util.io.CustomReader;
import com.ciklum.pavlov.util.io.CustomWriter;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ResourceBundle;

import static com.ciklum.pavlov.constants.UIKeys.PASSWORD_POLL;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RegistrationCommandTest {
    public static final String VALID_USER_NAME = "oleksandr";
    public static final String VALID_PASSWORD = "Oleksandr1@";

    @Mock
    private AccountService accountService;
    @Mock
    private ApplicationContext context;
    @Mock
    private RegistrationCommand registrationCommand;
    @Mock
    private CustomWriter customWriter;
    @Mock
    private CustomReader customReader;
    @Spy
    private final ResourceBundle resourceBundle = ResourceBundle.getBundle("application");


    @Before
    public void setUp() {
        when(context.getWriter()).thenReturn(customWriter);
        when(context.getReader()).thenReturn(customReader);
        when(context.getResourceBundle()).thenReturn(resourceBundle);
        registrationCommand = new RegistrationCommand(context, accountService);
    }

    @Test
    public void shouldPassUserToContextWhenValidCredentialsEntered() {
        when(customReader.readLine()).thenReturn(VALID_USER_NAME).thenReturn(VALID_PASSWORD);
        doReturn(StringUtils.EMPTY).when(resourceBundle).getString(PASSWORD_POLL);
        User user = new User(VALID_USER_NAME, VALID_PASSWORD);
        when(accountService.registerUser(user)).thenReturn(1l);

        boolean executeResult = registrationCommand.execute();

        verify(accountService).registerUser(user);
        verify(context).setCurrentUser(any(User.class));
        Assert.assertTrue(executeResult);
    }
}