package com.attijari.bankingservices.models;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class AuthenticationResponse implements Serializable {

    private final String jwt;

    public AuthenticationResponse(String jwt) {
        this.jwt = jwt;
    }

}
