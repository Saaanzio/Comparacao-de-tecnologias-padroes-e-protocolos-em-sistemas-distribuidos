package com.comparador.ComparadorTI.user.repository;


import com.projetoti.spring.soap.api.usersoap.UserRequest;
import com.projetoti.spring.soap.api.usersoap.UserResponse;

public interface UserSoapRepository {
    UserResponse createUser(UserRequest userRequest);
}
