package com.attijari.bankingservices.models;

import lombok.*;

import java.io.Serializable;
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationRequest implements Serializable {


    private String username;
    private String password;




}
