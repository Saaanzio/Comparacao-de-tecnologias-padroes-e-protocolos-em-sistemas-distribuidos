package com.comparador.ComparadorTI.user.service;


import com.projetoti.spring.soap.api.usersoap.UserRequest;
import com.projetoti.spring.soap.api.usersoap.UserResponse;

public interface UserSoapService {
    UserResponse createUser(UserRequest userRequest);
}
