package com.ciklum.pavlov.models;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class User {
    private long id;
    private String login;
    private String password;

    public User() {
    }

    public User(long id) {
        this.id = id;
    }

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }
}
