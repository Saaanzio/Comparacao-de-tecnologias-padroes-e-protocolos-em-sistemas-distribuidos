package com.comparador.ComparadorTI.user.service.Impl;

import com.comparador.ComparadorTI.user.service.UserService;
import com.comparador.ComparadorTI.user.model.User;
import com.comparador.ComparadorTI.user.repository.Impl.UserRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepositoryImpl userRepositoryImpl;

    @Override
    public List<User> getAllUsers() {
        return userRepositoryImpl.getAllUsers();
    }

    @Override
    public User getUser(int id) {
        return userRepositoryImpl.getUser(id);
    }

    @Override
    public User createUser(String name, String email) {
        return userRepositoryImpl.createUser(name, email);
    }
}
