package com.comparador.ComparadorTI.user.repository.Impl;

import com.comparador.ComparadorTI.user.model.User;
import com.comparador.ComparadorTI.user.repository.UserSoapRepository;

import com.projetoti.spring.soap.api.usersoap.UserRequest;
import com.projetoti.spring.soap.api.usersoap.UserResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;

@Repository
public class UserSoapRespoitoryImpl implements UserSoapRepository {
    private static Integer ID = 0;
    private final HashMap<Integer, UserResponse> users = new HashMap<>();

    @Override
    public UserResponse createUser(UserRequest userRequest) {
        if(users.values().stream().anyMatch(u -> u.getEmail().equals(userRequest.getEmail()))) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        UserResponse userResponse = new UserResponse();
        userResponse.setEmail(userRequest.getEmail());
        userResponse.setName(userRequest.getName());
        userResponse.setId(ID);
        users.put(ID, userResponse);
        ID += 1;
        return userResponse;
    }
}
