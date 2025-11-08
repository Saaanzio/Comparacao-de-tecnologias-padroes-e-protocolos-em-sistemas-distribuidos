package com.comparador.ComparadorTI.user.repository.Impl;

import com.comparador.ComparadorTI.user.model.User;
import com.comparador.ComparadorTI.user.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private static Integer ID = 0;
    private final HashMap<Integer, User> users = new HashMap<>();

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUser(int id) {
        return Optional.ofNullable(users.get(id)).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Override
    public User createUser(String name, String email) {
        User user = new User(ID, name, email);
        if(users.values().stream().anyMatch(u -> u.email().equals(email))){
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        users.put(ID, user);
        ID += 1;
        return user;
    }
}
