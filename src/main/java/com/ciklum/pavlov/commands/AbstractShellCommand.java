package com.ciklum.pavlov.commands;

import com.ciklum.pavlov.context.ApplicationContext;
import com.ciklum.pavlov.exceptions.ApplicationException;
import com.ciklum.pavlov.models.OptionDescription;
import lombok.NonNull;

import java.util.*;
import java.util.stream.Collectors;

import static com.ciklum.pavlov.util.PollUtil.SINGLE_DIGIT;
import static com.ciklum.pavlov.util.PollUtil.pollString;

public abstract class AbstractShellCommand extends AbstractCommand {
    public static final String PATTERN = "%s. %s";
    protected Map<OptionDescription, ICommand> optionMap;

    public AbstractShellCommand(ApplicationContext applicationContext, Map<OptionDescription, ICommand> optionMap) {
        super(applicationContext);
        this.optionMap = optionMap;
    }

    public boolean execute() {
        ResourceBundle resourceBundle =getResourceBundle();
        optionMap.forEach((key, value) -> writer.println(String.format(PATTERN, key.getOptionKey(), resourceBundle.getString(key.getDescription()))));
        String userAnswer = pollString(writer, reader, SINGLE_DIGIT, Optional.of(acceptableAnswers()), Optional.of(resourceBundle));
        return extractCommand(userAnswer).execute();
    }

    public static Set<String> extractAccessKeySet(Set<OptionDescription> map) {
        return map.stream().map(OptionDescription::getOptionKey).collect(Collectors.toSet());
    }

    protected ICommand extractCommand(@NonNull final String key) {
        Optional<ICommand> command = optionMap.entrySet().stream().filter(el -> key.equals(el.getKey().getOptionKey())).map(Map.Entry::getValue).findFirst();
        if (!command.isPresent()) {
            throw new ApplicationException("There is not any command for this key!");
        }
        return command.get();
    }

    protected Set<String> acceptableAnswers() {
        return optionMap.keySet().stream().map(OptionDescription::getOptionKey).collect(Collectors.toSet());
    }


}
