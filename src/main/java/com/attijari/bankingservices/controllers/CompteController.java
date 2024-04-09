package com.attijari.bankingservices.controllers;

import com.attijari.bankingservices.models.Compte;
import com.attijari.bankingservices.services.CompteService;
import com.attijari.bankingservices.utils.ManageDictEntities;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/comptes")
@Tag(name = "Compte",description = "Gestion des comptes des clients" )
public class CompteController {

    @Autowired
    private CompteService compteService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Compte>> getAccountByUserID(@PathVariable Long userId) {
        List<Compte> accounts = compteService.getAccountByUserID(userId);;
        if (!accounts.isEmpty()) {
            return ResponseEntity.ok(accounts);
        }
        return ResponseEntity.notFound().build();
    }
    @GetMapping("/userTypeCompte/{userId}/{typeCompte}")
    public ResponseEntity<List<Compte>> getAccountByUserIdAccountType(@PathVariable Long userId,@PathVariable String typeCompte) {
        List<Compte> accounts = compteService.getAccountByUserIdAndAccountType(userId,typeCompte);
        if (!accounts.isEmpty()) {
            return ResponseEntity.ok(accounts);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/searchEntitiesDict")
    public ResponseEntity<List<Compte>> getAccountByUserIdEntities(@RequestBody ManageDictEntities manager) {
        List<Compte> accounts = compteService.getAccountsByUserIdAndEntities(manager.getUserId(),manager.getEntitiesDic());
        if (!accounts.isEmpty()) {
            return ResponseEntity.ok(accounts);
        }
        return ResponseEntity.notFound().build();
    }
    @PostMapping("/balanceEntitiesDict")
    public ResponseEntity<Map<BigDecimal,Compte>> getBalanceByUserIdEntities(@RequestBody ManageDictEntities manager) {
        Map<BigDecimal,Compte> accounts = compteService.getBalanceByUserIdAndEntities(manager.getUserId(),manager.getEntitiesDic());
        if (!accounts.isEmpty()) {
            return ResponseEntity.ok(accounts);
        }
        return ResponseEntity.notFound().build();
    }
}

