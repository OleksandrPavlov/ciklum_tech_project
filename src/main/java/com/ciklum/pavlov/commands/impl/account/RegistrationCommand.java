package com.ciklum.pavlov.commands.impl.account;

import com.ciklum.pavlov.commands.AbstractCommand;
import com.ciklum.pavlov.context.ApplicationContext;
import com.ciklum.pavlov.models.User;
import com.ciklum.pavlov.services.AccountService;
import lombok.NonNull;

import java.util.Optional;
import java.util.ResourceBundle;

import static com.ciklum.pavlov.constants.UIKeys.LOGIN_POLL;
import static com.ciklum.pavlov.constants.UIKeys.PASSWORD_POLL;
import static com.ciklum.pavlov.util.PollUtil.*;

public class RegistrationCommand extends AbstractCommand {
    public static final String REGISTRATION_SUCCEED = "auth.registration.succeed";
    public static final String REGISTRATION_FAIL = "auth.registration.fail";

    private final AccountService accountService;

    @NonNull
    public RegistrationCommand(ApplicationContext context, AccountService accountService) {
        super(context);
        this.accountService = accountService;
    }

    @Override
    public boolean execute() {
        ResourceBundle resourceBundle =getResourceBundle();
        writer.println(resourceBundle.getString(LOGIN_POLL));
        String login = pollString(writer, reader, LOGIN, Optional.empty(), Optional.of(resourceBundle));
        writer.println(resourceBundle.getString(PASSWORD_POLL));
        String password = pollString(writer, reader, PASSWORD, Optional.empty(), Optional.of(resourceBundle));
        long userIdentifier = accountService.registerUser(new User(login, password));
        if (userIdentifier > -1) {
            context.setCurrentUser(new User(userIdentifier));
        }
        writer.println(userIdentifier >= 0 ? resourceBundle.getString(REGISTRATION_SUCCEED) : resourceBundle.getString(REGISTRATION_FAIL));
        return userIdentifier >= 0;
    }
}
