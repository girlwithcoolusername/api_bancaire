package com.attijari.bankingservices.controllers;

import com.attijari.bankingservices.models.Beneficiaire;
import com.attijari.bankingservices.services.BeneficiaireService;
import com.attijari.bankingservices.utils.ManageUserBeneficiaires;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/beneficiaires")
@Tag(name = "Bénéficiare", description = "Gestion des bénéficiares des clients")
public class BeneficiaireController {

    @Autowired
    private BeneficiaireService beneficiaireService;

    @PostMapping("/add")
    public ResponseEntity<String> addBeneficiary(@RequestBody ManageUserBeneficiaires manageUserBeneficiaires) {
        beneficiaireService.addBeneficiary(manageUserBeneficiaires.getUserId(), manageUserBeneficiaires.getBeneficiaire().getRib(), manageUserBeneficiaires.getBeneficiaire().getPrenom(), manageUserBeneficiaires.getBeneficiaire().getNom(), manageUserBeneficiaires.getBeneficiaire().getTypeBeneficiaire());
        return ResponseEntity.status(HttpStatus.CREATED).body("Beneficiary added successfully.");
    }

    @PostMapping("/user/names")
    public ResponseEntity<List<Beneficiaire>> getBeneficiariesByName(@RequestBody ManageUserBeneficiaires manager) {
        List<Beneficiaire> beneficiaries = beneficiaireService.getBeneficiaryByUserIdNames(manager.getUserId(), manager.getBeneficiaire().getPrenom(), manager.getBeneficiaire().getNom());
        if (!beneficiaries.isEmpty()) {
            return ResponseEntity.ok(beneficiaries);
        }
        return ResponseEntity.notFound().build();
    }


    @PutMapping("/update/rib")
    public ResponseEntity<String> updateBeneficiaryByRib(@RequestBody ManageUserBeneficiaires manager) {
        Beneficiaire beneficiaire = beneficiaireService.getBeneficiaryByUserIdAndRib(manager.getUserId(), manager.getOldRib());
        if (beneficiaire != null) {
            beneficiaireService.updateBeneficiaryByRib(manager.getUserId(), manager.getOldRib(), manager.getNewRib());
            return ResponseEntity.ok("Beneficiary rib updated successfully.");
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/update/names")
    public ResponseEntity<String> updateBeneficiaryByNames(@RequestBody ManageUserBeneficiaires manager) {
        List<Beneficiaire> beneficiaires = beneficiaireService.getBeneficiaryByUserIdNames(manager.getUserId(), manager.getBeneficiaire().getPrenom(), manager.getBeneficiaire().getNom());
        if (beneficiaires.size() > 1) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The user has more than one beneficiary with this name");
        } else if (beneficiaires.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No beneficiary found with the given name");
        } else {
            beneficiaireService.updateBeneficiaryByNames(manager.getUserId(), manager.getBeneficiaire().getPrenom(), manager.getBeneficiaire().getNom(), manager.getNewRib());
            return ResponseEntity.ok("Beneficiary names updated successfully.");
        }
    }

    @DeleteMapping("/delete/names")
    public ResponseEntity<String> deleteBeneficiaryByNames(@RequestBody ManageUserBeneficiaires manager) {
        List<Beneficiaire> beneficiares = beneficiaireService.getBeneficiaryByUserIdNames(manager.getUserId(), manager.getBeneficiaire().getPrenom(), manager.getBeneficiaire().getNom());
        if (beneficiares.size() > 1) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The user has more than one beneficiary with this name");
        } else if (beneficiares.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No beneficiary found with the given name");
        } else {
            beneficiaireService.deleteBeneficiaryByNames(manager.getUserId(), manager.getBeneficiaire().getPrenom(), manager.getBeneficiaire().getNom());
            return ResponseEntity.ok("Beneficiary deleted successfully.");
        }
    }

    @DeleteMapping("/delete/rib")
    public ResponseEntity<String> deleteBeneficiaryByRib(@RequestBody ManageUserBeneficiaires manager) {
        Beneficiaire beneficiaire = beneficiaireService.getBeneficiaryByUserIdAndRib(manager.getUserId(), manager.getBeneficiaire().getRib());
        if (beneficiaire != null) {
            beneficiaireService.deleteBeneficiaryByRib(manager.getUserId(), manager.getBeneficiaire().getRib());
            return ResponseEntity.ok("Beneficiary deleted successfully.");
        }
        return ResponseEntity.notFound().build();
    }
}
