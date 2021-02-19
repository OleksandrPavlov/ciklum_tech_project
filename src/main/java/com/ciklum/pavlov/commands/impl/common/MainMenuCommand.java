package com.ciklum.pavlov.commands.impl.common;

import com.ciklum.pavlov.commands.AbstractShellCommand;
import com.ciklum.pavlov.commands.ICommand;
import com.ciklum.pavlov.context.ApplicationContext;
import com.ciklum.pavlov.models.OptionDescription;

import java.util.Map;

public class MainMenuCommand extends AbstractShellCommand {
    public static final String MAIN_MENU_HEADER = "main.header";

    public MainMenuCommand(ApplicationContext context, Map<OptionDescription, ICommand> optionDescriptionICommandMap) {
        super(context, optionDescriptionICommandMap);
    }

    @Override
    public boolean execute() {
        writer.println(getResourceBundle().getString(MAIN_MENU_HEADER));
        return super.execute();
    }
}
