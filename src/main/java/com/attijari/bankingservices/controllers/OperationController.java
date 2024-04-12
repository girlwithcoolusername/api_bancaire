package com.attijari.bankingservices.controllers;

import com.attijari.bankingservices.models.Beneficiaire;
import com.attijari.bankingservices.models.Compte;
import com.attijari.bankingservices.models.Operation;
import com.attijari.bankingservices.services.BeneficiaireService;
import com.attijari.bankingservices.services.CompteService;
import com.attijari.bankingservices.services.OperationService;
import com.attijari.bankingservices.utils.ManageDictEntities;
import com.attijari.bankingservices.utils.ManageUserOperations;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/operations")
@Tag(name = "Opérations", description = "Gestion des opérations des clients")
public class OperationController {

    private final OperationService OperationService;
    private final CompteService compteService;
    private final BeneficiaireService beneficiaireService;

    @Autowired
    public OperationController(com.attijari.bankingservices.services.OperationService operationService, CompteService compteService, BeneficiaireService beneficiaireService) {
        OperationService = operationService;
        this.compteService = compteService;
        this.beneficiaireService = beneficiaireService;
    }


    @GetMapping("/user/{userId}")
    @io.swagger.v3.oas.annotations.Operation(summary = "Méthode pour récupérer les transactions par utilisateur")
    public ResponseEntity<List<Operation>> getTransactionsByUserId(@PathVariable Long userId) {
        List<Operation> operations = OperationService.getTransactionByUserId(userId);
        if (operations != null) {
            return ResponseEntity.ok(operations);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/searchEntitiesDict")
    @io.swagger.v3.oas.annotations.Operation(summary = "Méthode pour récupérer les transactions d'un utilisateur donné avec des attributs spécifiques")
    public ResponseEntity<List<Operation>> getTransactionByUserIdEntities(@RequestBody ManageDictEntities manager) {
        List<Operation> operations = OperationService.getTransactionByUserIdEntities(manager.getUserId(), manager.getEntitiesDic());
        if (!operations.isEmpty()) {
            return ResponseEntity.ok(operations);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/addByAccountType")
    @io.swagger.v3.oas.annotations.Operation(summary = "Méthode pour ajouter une transaction par spécification du type de compte")
    public ResponseEntity<String> addTransactionByAccountType(@RequestBody ManageUserOperations m) {
        List<Compte> accounts = compteService.getAccountByUserIdAndAccountType(m.getUserId(), m.getCompte().getTypeCompte());
        if (!accounts.isEmpty()) {
            if (accounts.size() > 1) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Provide account number.");
            }
            OperationService.addTransactionByAccountType(m.getUserId(), m.getCompte().getTypeCompte(), m.getBeneficiaire().getRib(), m.getOperation().getMontant(), m.getOperation().getMotif(), m.getOperation().getCategorieOperation());
            return ResponseEntity.status(HttpStatus.CREATED).body("Operation added successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Account not found.");
        }

    }

    @PostMapping("/addByAccountTypeBeneficiaryNames")
    @io.swagger.v3.oas.annotations.Operation(summary = "Méthode pour ajouter une transaction par spécification des noms de bénéficiare et le type de compte")
    public ResponseEntity<String> addTransactionByAccountTypeAndBenefeciaryNames(@RequestBody ManageUserOperations m) {
        List<Beneficiaire> beneficiaries = beneficiaireService.getBeneficiaryByUserIdNames(m.getUserId(), m.getBeneficiaire().getPrenom(), m.getBeneficiaire().getNom());
        if (!beneficiaries.isEmpty()) {
            if (beneficiaries.size() > 1) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Provide beneficiary RIB too many beneficiaries with the same name .");
            }
            Beneficiaire beneficiaire = beneficiaries.get(0);
            m.setBeneficiaire(beneficiaire);
            return addTransactionByAccountType(m);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Beneficiary not found.");
        }
    }

    @PostMapping("/addByAccountNum")
    @io.swagger.v3.oas.annotations.Operation(summary = "Méthode pour ajouter une transaction par spécification du numéro de compte")
    public ResponseEntity<String> addTransactionByAccountNum(@RequestBody ManageUserOperations m) {
        Optional<Compte> account = compteService.getAccountByUserIdAndAccountNum(m.getUserId(), m.getCompte().getNumeroCompte());
        if (account.isPresent()) {
            OperationService.addTransactionByAccountNum(m.getUserId(), m.getCompte().getNumeroCompte(), m.getBeneficiaire().getRib(),m.getOperation().getMontant(), m.getOperation().getMotif(), m.getOperation().getCategorieOperation());
            return ResponseEntity.status(HttpStatus.CREATED).body("Operation added successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Account not found.");
        }

    }
    @PostMapping("/addByAccountNumBeneficiaryNames")
    @io.swagger.v3.oas.annotations.Operation(summary = "Méthode pour ajouter une transaction par spécification des noms de bénéficiare et le numéro de compte")
    public ResponseEntity<String> addTransactionByAccountNumAndBenefeciaryNames(@RequestBody ManageUserOperations m) {
        List<Beneficiaire> beneficiaries = beneficiaireService.getBeneficiaryByUserIdNames(m.getUserId(), m.getBeneficiaire().getPrenom(), m.getBeneficiaire().getNom());
        if (!beneficiaries.isEmpty()) {
            if (beneficiaries.size() > 1) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Provide beneficiary RIB too many beneficiaries with the same name .");
            }
            Beneficiaire beneficiaire = beneficiaries.get(0);
            m.setBeneficiaire(beneficiaire);
            return addTransactionByAccountNum(m);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Beneficiary not found.");
        }
    }

}

