package com.comparador.ComparadorTI.user.controller;

import com.comparador.ComparadorTI.user.service.UserSoapService;

import com.projetoti.spring.soap.api.usersoap.UserRequest;
import com.projetoti.spring.soap.api.usersoap.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class UserSoapEndpoint {
    private static final String NAMESPACE = "http://www.projetoti.com/spring/soap/api/userSoap";

    @Autowired
    private UserSoapService userSoapService;

    @PayloadRoot(namespace = NAMESPACE,localPart= "UserRequest")
    @ResponsePayload
    public UserResponse createUser(@RequestPayload UserRequest userRequest) {
        System.out.println("🚀 Entrou no endpoint SOAP");
        return userSoapService.createUser(userRequest);
    }
}
