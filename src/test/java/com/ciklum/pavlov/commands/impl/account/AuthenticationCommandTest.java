package com.ciklum.pavlov.commands.impl.account;

import com.ciklum.pavlov.commands.ICommand;
import com.ciklum.pavlov.context.ApplicationContext;
import com.ciklum.pavlov.models.OptionDescription;
import com.ciklum.pavlov.util.io.CustomReader;
import com.ciklum.pavlov.util.io.CustomWriter;
import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import static com.ciklum.pavlov.constants.UIKeys.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AuthenticationCommandTest {
    @Mock
    private ApplicationContext context;
    @Mock
    private RegistrationCommand registrationCommand;
    @Mock
    private SignInCommand signInCommand;
    @Mock
    private CustomWriter writer;
    @Mock
    private CustomReader reader;
    @Spy
    private ResourceBundle resourceBundle = ResourceBundle.getBundle("application");
    AuthenticationCommand authenticationCommand;

    @Before
    public void init() {
        Map<OptionDescription, ICommand> optionDescriptionICommandMap = new HashMap<>();
        optionDescriptionICommandMap.put(new OptionDescription(ONE, REGISTRATION_HEADER), registrationCommand);
        optionDescriptionICommandMap.put(new OptionDescription(TWO, SIGN_IN_HEADER), signInCommand);
        when(context.getWriter()).thenReturn(writer);
        when(context.getReader()).thenReturn(reader);
        when(context.getResourceBundle()).thenReturn(resourceBundle);
        authenticationCommand = new AuthenticationCommand(context, optionDescriptionICommandMap);
    }

    @Test
    public void shouldExecuteChosenCommand() {
        doReturn(StringUtils.EMPTY).when(resourceBundle).getString(SIGN_IN_HEADER);
        when(reader.readLine()).thenReturn(ONE);
        authenticationCommand.execute();
        verify(registrationCommand).execute();
    }
}