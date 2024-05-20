package com.attijari.bankingservices.controllers;

import com.attijari.bankingservices.models.Utilisateur;
import com.attijari.bankingservices.services.UtilisateurService;
import com.attijari.bankingservices.utils.ManageFeatures;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@Tag(name = "Utilisateur",description = "Gestion des utilisateurs")
public class UtilisateurController {

    @Autowired
    private  UtilisateurService utilisateurService;
    @GetMapping("/authenticate")
    @Operation(summary = "Méthode pour authentifier les utilisateurs")
    public ResponseEntity<Utilisateur> authenticateUser(@RequestParam String username, @RequestParam String password) {
        Utilisateur utilisateur = utilisateurService.authenticateUser(username, password);
        if (utilisateur != null) {
            return ResponseEntity.ok(utilisateur);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }
    @PostMapping("/authenticate-voice-print")
    @Operation(summary = "Méthode pour authentifier les utilisateurs")
    public ResponseEntity<Utilisateur> authenticateUserByVoicePrint(@RequestBody ManageFeatures manager) {
        Utilisateur utilisateur = utilisateurService.authenticateUserByVoicePrint(manager.getFeatures());
        if (utilisateur != null) {
            return ResponseEntity.ok(utilisateur);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @GetMapping("/{userId}")
    @Operation(summary = "Méthode pour récupérer un utilisateur à partir de son id")
    public ResponseEntity<Utilisateur> getUserById(@PathVariable Long userId) {
        Utilisateur utilisateur = utilisateurService.getUserById(userId);
        if (utilisateur != null) {
            return ResponseEntity.ok(utilisateur);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
