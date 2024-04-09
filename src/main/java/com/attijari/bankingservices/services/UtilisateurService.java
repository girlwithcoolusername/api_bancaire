package com.attijari.bankingservices.services;

import com.attijari.bankingservices.models.Utilisateur;

public interface UtilisateurService {
    Utilisateur authenticateUser(String username, String password);
    Utilisateur getUserById(Long userId);

}
