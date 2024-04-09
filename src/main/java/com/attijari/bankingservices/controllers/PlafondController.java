package com.attijari.bankingservices.controllers;


import com.attijari.bankingservices.models.Carte;
import com.attijari.bankingservices.services.CarteService;
import com.attijari.bankingservices.services.PlafondService;
import com.attijari.bankingservices.utils.ManageUserCardLimits;
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
@RequestMapping("/plafond")
@Tag(name = "Plafond des cartes bancaires",description = "Gestion des plafonds des cartes bancaires des clients" )
public class PlafondController {


    private final PlafondService plafondService;
    private final CarteService carteService;

    @Autowired
    public PlafondController(PlafondService plafondService, CarteService carteService) {
        this.plafondService = plafondService;
        this.carteService = carteService;
    }

    @PostMapping("/updateByCardType")
    public ResponseEntity<String> updateCardLimitByCardType(@RequestBody ManageUserCardLimits m) {
        List<Carte> cartes = carteService.getCardByUserIdAndType(m.getUserId(), m.getTypeCarte());
        if (!cartes.isEmpty()) {
            if (cartes.size() > 1) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Provide a single card number.");
            }
            plafondService.updateCardLimitByCardType(m.getUserId(), m.getTypeCarte(),m.getPlafond(), m.getTypePlafond(),m.getDuration());
            return ResponseEntity.status(HttpStatus.CREATED).body("Limit card updated successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Card not found for the given user and account type.");
        }
    }
    @PostMapping("/updateByCardNum")
    public ResponseEntity<String> updateCardLimitByCardNum(@RequestBody ManageUserCardLimits m) {
        Optional<Carte> cartes = carteService.getCardByNum(m.getNumeroCarte());
        if (cartes.isPresent()) {
            plafondService.updateCardLimitByCardNum(m.getNumeroCarte(),m.getPlafond(), m.getTypePlafond(),m.getDuration());
            return ResponseEntity.status(HttpStatus.CREATED).body("Limit card updated successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Card not found for the given number.");
        }
    }

}
