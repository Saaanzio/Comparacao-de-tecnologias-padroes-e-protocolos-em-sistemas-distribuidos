package com.comparador.ComparadorTI.user.repository;

import com.comparador.ComparadorTI.user.model.User;

import java.util.List;

public interface UserRepository {
    List<User> getAllUsers();
    User getUser(int id);
    User createUser(String name, String email);
}
