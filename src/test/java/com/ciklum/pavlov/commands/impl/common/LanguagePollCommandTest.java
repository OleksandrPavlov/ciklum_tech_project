package com.ciklum.pavlov.commands.impl.common;

import com.ciklum.pavlov.context.ApplicationContext;
import com.ciklum.pavlov.models.OptionDescription;
import com.ciklum.pavlov.util.io.CustomReader;
import com.ciklum.pavlov.util.io.CustomWriter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.ResourceBundle;

import static com.ciklum.pavlov.constants.UIKeys.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LanguagePollCommandTest {
    @Mock
    private ApplicationContext applicationContext;
    @Mock
    private CustomWriter writer;
    @Mock
    private CustomReader reader;
    @Spy
    private final ResourceBundle resourceBundle = ResourceBundle.getBundle("application");

    private final LinkedHashMap<OptionDescription, Locale> supportedLocales = new LinkedHashMap<>();

    private LanguagePollCommand languagePollCommand;

    @Before
    public void setUp() {
        when(applicationContext.getResourceBundle()).thenReturn(resourceBundle);
        when(applicationContext.getReader()).thenReturn(reader);
        when(applicationContext.getWriter()).thenReturn(writer);
        languagePollCommand = new LanguagePollCommand(applicationContext, supportedLocales);
    }

    @Test
    public void shouldSetLocaleToContextAccordingUserInput() {
        supportedLocales.put(new OptionDescription(ONE, LANGUAGE_ENGLISH), new Locale("en_EN"));
        supportedLocales.put(new OptionDescription(TWO, LANGUAGE_RUSSIAN), new Locale("ru_RU"));
        when(reader.readLine()).thenReturn(ONE);
        languagePollCommand.execute();
        verify(applicationContext).setLocale(new Locale("en_EN"));
    }

}