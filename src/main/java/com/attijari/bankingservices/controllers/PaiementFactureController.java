package com.attijari.bankingservices.controllers;

import com.attijari.bankingservices.models.Compte;
import com.attijari.bankingservices.models.Facture;
import com.attijari.bankingservices.services.CompteService;
import com.attijari.bankingservices.services.FactureService;
import com.attijari.bankingservices.services.PaiementFactureService;
import com.attijari.bankingservices.utils.ManageUserInvoicePayments;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/paiement-facture")
@Tag(name = "Paiements de factures",description = "Gestion des paiements des clients" )
public class PaiementFactureController {

    private final PaiementFactureService paiementFactureService;
    private final CompteService compteService;
    private final FactureService factureService;

    @Autowired
    public PaiementFactureController(PaiementFactureService paiementFactureService, CompteService compteService, FactureService factureService) {
        this.paiementFactureService = paiementFactureService;
        this.compteService = compteService;
        this.factureService = factureService;
    }

    @PostMapping("/addInvoiceByAccountType")
    public ResponseEntity<String> addTransactionByAccountType(@RequestBody ManageUserInvoicePayments m) {
        Optional<Facture> invoice = factureService.getInvoiceByNumber(m.getNumeroFacture());
        if (invoice.isPresent()) {
            List<Compte> accounts = compteService.getAccountByUserIdAndAccountType(m.getUserId(), m.getTypeCompte());
            if (!accounts.isEmpty()) {
                if (accounts.size() > 1) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Provide a single account number.");
                }
                paiementFactureService.addInvoiceTransactionByAccountType(m.getUserId(), m.getNumeroFacture(), m.getTypeCompte());
                return ResponseEntity.status(HttpStatus.CREATED).body("Invoice Payment added successfully.");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Account not found for the given user and account type.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invoice not found.");
        }
    }
    @PostMapping("/addInvoiceByAccountNum")
    public ResponseEntity<String> addTransactionByAccountNum(@RequestBody ManageUserInvoicePayments m) {
        Optional<Facture> invoice = factureService.getInvoiceByNumber(m.getNumeroFacture());
        if(invoice.isPresent()){
            Optional<Compte> accounts = compteService.getAccountByUserIdAndAccountNum(m.getUserId(), m.getNumeroCompte());
            if (accounts.isPresent()) {
                paiementFactureService.addInvoiceTransactionByAccountNum(m.getNumeroFacture(), m.getNumeroCompte());
                return ResponseEntity.status(HttpStatus.CREATED).body("Invoice Payment added successfully.");
            }
            else{
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Account not found.");
            }
        }
        else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invoice not found.");
        }

    }
}
