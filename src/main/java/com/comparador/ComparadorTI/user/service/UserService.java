package com.comparador.ComparadorTI.user.service;

import com.comparador.ComparadorTI.user.model.User;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();
    User getUser(int id);

    User createUser(String name, String email);
}
