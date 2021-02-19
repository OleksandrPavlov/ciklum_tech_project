package com.ciklum.pavlov.services;

import com.ciklum.pavlov.models.User;

import java.util.Optional;

public interface AccountService {

    long registerUser(User user);

    Optional<User> signUser(User user);
}
