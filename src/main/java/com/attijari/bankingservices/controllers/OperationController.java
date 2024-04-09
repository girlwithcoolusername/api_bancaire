package com.attijari.bankingservices.controllers;

import com.attijari.bankingservices.models.Compte;
import com.attijari.bankingservices.models.Operation;
import com.attijari.bankingservices.services.CompteService;
import com.attijari.bankingservices.services.OperationService;
import com.attijari.bankingservices.utils.ManageDictEntities;
import com.attijari.bankingservices.utils.ManageUserOperations;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/operations")
@Tag(name = "Opérations",description = "Gestion des opérations des clients" )
public class OperationController {

    private final OperationService OperationService;
    private final CompteService compteService;

    @Autowired
    public OperationController(com.attijari.bankingservices.services.OperationService operationService, CompteService compteService) {
        OperationService = operationService;
        this.compteService = compteService;
    }


    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Operation>> getTransactionsByUserId(@PathVariable Long userId) {
        List<Operation> operations = OperationService.getTransactionByUserId(userId);
        if (operations != null) {
            return ResponseEntity.ok(operations);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/searchEntitiesDict")
    public ResponseEntity<List<Operation>> getTransactionByUserIdEntities(@RequestBody ManageDictEntities manager) {
        List<Operation> operations = OperationService.getTransactionByUserIdEntities(manager.getUserId(), manager.getEntitiesDic());
        if (!operations.isEmpty()) {
            return ResponseEntity.ok(operations);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/addByAccountType")
    public ResponseEntity<String> addTransactionByAccountType(@RequestBody ManageUserOperations m) {
        List<Compte> accounts = compteService.getAccountByUserIdAndAccountType(m.getUserId(), m.getTypeCompte());
        if (!accounts.isEmpty()) {
            if (accounts.size() > 1) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Provide account number.");
            }
            OperationService.addTransactionByAccountType(m.getUserId(), m.getTypeCompte(), m.getRibBeneficiaire(), m.getMontant(), m.getMotif(), m.getType());
            return ResponseEntity.status(HttpStatus.CREATED).body("Operation added successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Account not found.");
        }

    }

    @PostMapping("/addByAccountNum")
    public ResponseEntity<String> addTransactionByAccountNum(@RequestBody ManageUserOperations m) {
        Optional<Compte> account = compteService.getAccountByUserIdAndAccountNum(m.getUserId(),m.getNumeroCompte());
        if (account.isPresent()) {
            OperationService.addTransactionByAccountNum(m.getUserId(), m.getNumeroCompte(), m.getRibBeneficiaire(), m.getMontant(), m.getMotif(), m.getType());
            return ResponseEntity.status(HttpStatus.CREATED).body("Operation added successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Account not found.");
        }

    }

}

