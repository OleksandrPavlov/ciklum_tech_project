package com.ciklum.pavlov.commands.impl.common;

import com.ciklum.pavlov.commands.AbstractCommand;
import com.ciklum.pavlov.commands.AbstractShellCommand;
import com.ciklum.pavlov.context.ApplicationContext;
import com.ciklum.pavlov.exceptions.ApplicationException;
import com.ciklum.pavlov.models.OptionDescription;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

import static com.ciklum.pavlov.util.PollUtil.SINGLE_DIGIT;
import static com.ciklum.pavlov.util.PollUtil.pollString;

@Slf4j
public class LanguagePollCommand extends AbstractCommand {
    public static final String LANGUAGE_SHELL_HEADER = "lang.shell.header";

    public static final String OPTION_REPRESENTATION_FORMAT = "%s. %s;";
    private final Map<OptionDescription, Locale> supportedLocales;

    public LanguagePollCommand(ApplicationContext context, LinkedHashMap<OptionDescription, Locale> supportedLocales) {
        super(context);
        this.supportedLocales = supportedLocales;
    }

    @Override
    public boolean execute() {
        String userInput;
        ResourceBundle resourceBundle =getResourceBundle();
        writer.println(resourceBundle.getString(LANGUAGE_SHELL_HEADER));
        supportedLocales.forEach((key, value) -> writer.println(String.format(OPTION_REPRESENTATION_FORMAT, key.getOptionKey(), key.getDescription())));
        userInput = pollString(writer, reader, SINGLE_DIGIT, Optional.of(AbstractShellCommand.extractAccessKeySet(supportedLocales.keySet())), Optional.of(resourceBundle));
        context.setLocale(getLocaleByAnswer(userInput));
        return Boolean.TRUE;
    }

    private Locale getLocaleByAnswer(final String localKey) {
        Optional<Locale> locale = supportedLocales.
                entrySet()
                .stream()
                .filter((entry) -> entry.getKey().getOptionKey().equals(localKey))
                .map(Map.Entry::getValue)
                .findAny();
        if (!locale.isPresent()) {
            log.error("Invalid local key has been passed!");
            throw new ApplicationException();
        }
        return locale.get();
    }


}
