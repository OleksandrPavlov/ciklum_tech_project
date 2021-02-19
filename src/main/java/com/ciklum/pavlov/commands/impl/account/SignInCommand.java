package com.ciklum.pavlov.commands.impl.account;

import com.ciklum.pavlov.commands.AbstractCommand;
import com.ciklum.pavlov.context.ApplicationContext;
import com.ciklum.pavlov.models.User;
import com.ciklum.pavlov.services.AccountService;

import java.util.Optional;
import java.util.ResourceBundle;

import static com.ciklum.pavlov.constants.UIKeys.LOGIN_POLL;
import static com.ciklum.pavlov.constants.UIKeys.PASSWORD_POLL;
import static com.ciklum.pavlov.util.PollUtil.*;


public class SignInCommand extends AbstractCommand {
    public static final String ENTRANCE_SUCCEED = "auth.signing.success";
    public static final String ENTRANCE_FAILED = "auth.signing.fail";
    private final AccountService accountService;

    public SignInCommand(ApplicationContext applicationContext, AccountService accountService) {
        super(applicationContext);
        this.accountService = accountService;
    }

    @Override
    public boolean execute() {
        ResourceBundle resourceBundle =getResourceBundle();
        writer.println(resourceBundle.getString(LOGIN_POLL));
        String login = pollString(writer, reader, LOGIN, Optional.empty(), Optional.of(resourceBundle));
        writer.println(resourceBundle.getString(PASSWORD_POLL));
        String password = pollString(writer, reader, PASSWORD, Optional.empty(), Optional.of(resourceBundle));
        Optional<User> user = accountService.signUser(new User(login, password));
        writer.println(user.isPresent() ? resourceBundle.getString(ENTRANCE_SUCCEED) : resourceBundle.getString(ENTRANCE_FAILED));
        user.ifPresent(value -> context.setCurrentUser(value));
        return user.isPresent();
    }
}
