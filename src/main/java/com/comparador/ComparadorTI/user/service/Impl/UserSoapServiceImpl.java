package com.comparador.ComparadorTI.user.service.Impl;

import com.comparador.ComparadorTI.user.repository.Impl.UserSoapRespoitoryImpl;
import com.comparador.ComparadorTI.user.repository.UserSoapRepository;
import com.comparador.ComparadorTI.user.service.UserSoapService;

import com.projetoti.spring.soap.api.usersoap.UserRequest;
import com.projetoti.spring.soap.api.usersoap.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserSoapServiceImpl implements UserSoapService {
    private final UserSoapRepository userSoapRepository;
    public UserResponse createUser(UserRequest userRequest) {
        return userSoapRepository.createUser(userRequest);
    }
}
