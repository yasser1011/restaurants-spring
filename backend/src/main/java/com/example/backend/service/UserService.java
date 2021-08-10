package com.example.backend.service;

import com.example.backend.models.User;

import java.util.List;

public interface UserService {
    UserLoginResponse saveUser(User user) throws Exception;
    User getUser(String username);
    User getUserByToken();
    List<User> getUsers();
}
