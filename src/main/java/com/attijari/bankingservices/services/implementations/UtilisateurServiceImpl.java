package com.attijari.bankingservices.services.implementations;

import com.attijari.bankingservices.models.Utilisateur;
import com.attijari.bankingservices.repositories.UtilisateurRepository;
import com.attijari.bankingservices.services.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UtilisateurServiceImpl implements UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;

    @Autowired
    public UtilisateurServiceImpl(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    @Override
    public Utilisateur authenticateUser(String username, String password) {
        Utilisateur utilisateur = utilisateurRepository.findByUsername(username);
        if (utilisateur != null && utilisateur.getPassword().equals(password)) {
            return utilisateur;
        } else {
            return null;
        }
    }

    @Override
    public Utilisateur getUserById(Long userId) {

        return utilisateurRepository.findById(userId).orElse(null);
    }
}
