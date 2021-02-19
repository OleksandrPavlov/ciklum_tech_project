package com.ciklum.pavlov.dao;

import com.ciklum.pavlov.models.User;

import java.util.Optional;

public interface UserDao {
    long add(User user);

    Optional<User> findByLogin(String login);
}
