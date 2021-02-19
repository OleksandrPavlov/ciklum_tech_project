package com.ciklum.pavlov.services.impl;

import com.ciklum.pavlov.dao.UserDao;
import com.ciklum.pavlov.jdbc.TransactionManager;
import com.ciklum.pavlov.models.User;
import com.ciklum.pavlov.services.AccountService;

import java.util.Optional;

public class AccountServiceImpl implements AccountService {
    private final UserDao userDao;
    private final TransactionManager transactionManager;

    public AccountServiceImpl(UserDao userDao, TransactionManager transactionManager) {
        this.userDao = userDao;
        this.transactionManager = transactionManager;
    }

    @Override
    public long registerUser(final User user) {
        return transactionManager.executeInTransaction(() -> userDao.add(user));
    }

    @Override
    public Optional<User> signUser(User user) {
        Optional<User> userFromDB = transactionManager.executeInTransaction(() -> userDao.findByLogin(user.getLogin()));
        return userFromDB.isPresent() && user.getPassword().equals(userFromDB.get().getPassword()) ? userFromDB : Optional.empty();
    }

}
