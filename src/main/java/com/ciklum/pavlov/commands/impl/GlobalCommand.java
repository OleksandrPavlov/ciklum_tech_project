package com.ciklum.pavlov.commands.impl;

import com.ciklum.pavlov.commands.ICommand;
import com.ciklum.pavlov.commands.impl.account.AuthenticationCommand;
import com.ciklum.pavlov.commands.impl.common.LanguagePollCommand;
import com.ciklum.pavlov.commands.impl.common.MainMenuCommand;

public class GlobalCommand implements ICommand {
    private final AuthenticationCommand authenticationCommand;
    private final LanguagePollCommand languagePollCommand;
    private final MainMenuCommand mainMenuCommand;

    public GlobalCommand(AuthenticationCommand authenticationCommand, LanguagePollCommand languagePollCommand, MainMenuCommand mainMenuCommand) {
        this.authenticationCommand = authenticationCommand;
        this.languagePollCommand = languagePollCommand;
        this.mainMenuCommand = mainMenuCommand;
    }

    @Override
    public boolean execute() {
        runWhileNotSucceed(languagePollCommand);
        runWhileNotSucceed(authenticationCommand);
        runWhileSucceed(mainMenuCommand);
        return Boolean.TRUE;
    }

    private void runWhileNotSucceed(ICommand command) {
        while (!command.execute()) ;
    }

    private void runWhileSucceed(ICommand command) {
        while (command.execute()) ;
    }
}
