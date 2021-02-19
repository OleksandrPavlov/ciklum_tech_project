package com.ciklum.pavlov.commands.impl.account;

import com.ciklum.pavlov.commands.AbstractShellCommand;
import com.ciklum.pavlov.commands.ICommand;
import com.ciklum.pavlov.context.ApplicationContext;
import com.ciklum.pavlov.models.OptionDescription;

import java.util.Map;
import java.util.ResourceBundle;

public class AuthenticationCommand extends AbstractShellCommand {
    public static final String AUTHENTICATION_SHELL_HEADER = "auth.shell.header";

    public AuthenticationCommand(ApplicationContext applicationContext, Map<OptionDescription, ICommand> optionMap) {
        super(applicationContext, optionMap);
    }

    @Override
    public boolean execute() {
        ResourceBundle resourceBundle = getResourceBundle();
        writer.println(resourceBundle.getString(AUTHENTICATION_SHELL_HEADER));
        return super.execute();
    }
}
