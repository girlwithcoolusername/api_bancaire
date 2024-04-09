package com.attijari.bankingservices.controllers;

import com.attijari.bankingservices.models.Agence;
import com.attijari.bankingservices.services.AgenceService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/agences")
@Tag(name = "Agence", description = "Gestion des infos d'agences et de leurs géolocalisations")
public class AgenceController {

    @Autowired
    private AgenceService agenceService;

    @GetMapping("/")
    public ResponseEntity<List<Agence>> getAllAgencies() {
        List<Agence> agencies = agenceService.getAllAgencies();
        if (!agencies.isEmpty()) {
            return ResponseEntity.ok(agencies);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/userAddress/{userId}")
    public ResponseEntity<List<Agence>> getAgenciesByRegistredAddress(@PathVariable Long userId) {
        List<Agence> agencies = agenceService.getAgenciesByRegistredAddress(userId);
        if (!agencies.isEmpty()) {
            return ResponseEntity.ok(agencies);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Agence>> getAgenciesByUserId(@PathVariable Long userId) {
        List<Agence> agencies = agenceService.getAgenciesByUserId(userId);
        if (!agencies.isEmpty()) {
            return ResponseEntity.ok(agencies);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/location/{lat}/{lng}")
    public ResponseEntity<List<Agence>> getAgenciesByLocation(@PathVariable BigDecimal lat, @PathVariable BigDecimal lng) {
        List<Agence> agencies = agenceService.getAgenciesByUserLocation(lat, lng);
        if (!agencies.isEmpty()) {
            return ResponseEntity.ok(agencies);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
