package com.attijari.bankingservices.controllers;

import com.attijari.bankingservices.models.Carte;
import com.attijari.bankingservices.services.CarteService;
import com.attijari.bankingservices.utils.ManageDictEntities;
import com.attijari.bankingservices.utils.ManageUserCartes;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/cartes")
@Tag(name = "Carte", description = "Gestion des cartes de clients")
public class CarteController {

    @Autowired
    private CarteService carteService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Carte>> getCardByUser(@PathVariable Long userId) {
        List<Carte> cartes = carteService.getCardByUserId(userId);
        if (cartes != null) {
            return ResponseEntity.ok(cartes);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/userCardsByType/{userId}/{typeCard}")
    public ResponseEntity<List<Carte>> getCardByType(@PathVariable Long userId, @PathVariable String typeCard) {
        List<Carte> cartes = carteService.getCardByUserIdAndType(userId, typeCard);
        if (cartes != null) {
            return ResponseEntity.ok(cartes);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/searchEntitiesDict")
    public ResponseEntity<List<Carte>> getCardByUserIdAndEntities(@RequestBody ManageDictEntities manager) {
        List<Carte> cartes = carteService.getCardByUserIdAndEntities(manager.getUserId(), manager.getEntitiesDic());
        if (!cartes.isEmpty()) {
            return ResponseEntity.ok(cartes);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/updateByCardNum")
    public ResponseEntity<String> updateCardServicesByCardNum(@RequestBody ManageUserCartes manager) {
        Long numeroCarte = manager.getCarte().getNumeroCarte();
        Optional<Carte> card = carteService.getCardByNum(numeroCarte);
        if (card.isPresent()) {
            carteService.updateCardServicesByCardNum(manager.getUserId(), manager.getCarte().getNumeroCarte(), manager.getServices(), manager.getStatus());
            return ResponseEntity.status(HttpStatus.CREATED).body("Card updated successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Card doesn't exist");
        }
    }

    @PutMapping("/updateByCardType")
    public ResponseEntity<String> updateCardServicesByCardType(@RequestBody ManageUserCartes manager) {
        List<Carte> cartes = carteService.getCardByUserIdAndType(manager.getUserId(), manager.getCarte().getTypeCarte());
        if (!cartes.isEmpty()) {
            if (cartes.size() == 1) {
                carteService.updateCardServicesByCardType(manager.getUserId(), manager.getCarte().getTypeCarte(), manager.getServices(), manager.getStatus());
                return ResponseEntity.status(HttpStatus.CREATED).body("Card updated successfully.");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Specify card number.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Card not found.");
        }
    }

    @PutMapping("/opposeByCardNum")
    public ResponseEntity<String> opposeCardByCardNum(@RequestBody ManageUserCartes manager) {
        Long numeroCarte = manager.getCarte().getNumeroCarte();
        Optional<Carte> card = carteService.getCardByNum(numeroCarte);
        if (card.isPresent()) {
            Carte carte = card.get();
            if (carte.getStatutCarte().equals("opposée")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Card already opposed");
            } else {
                carteService.opposeCardByCardNum(manager.getUserId(), manager.getCarte().getNumeroCarte(), manager.getRaisonsOpposition());
                return ResponseEntity.status(HttpStatus.CREATED).body("Card opposed successfully.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Card doesn't exist");
        }
    }

    @PutMapping("/opposeByCardType")
    public ResponseEntity<String> opposeCardByCardType(@RequestBody ManageUserCartes manager) {
        List<Carte> cartes = carteService.getCardByUserIdAndType(manager.getUserId(), manager.getCarte().getTypeCarte());
        if (!cartes.isEmpty()) {
            if (cartes.size() == 1) {
                String status = cartes.get(0).getStatutCarte();
                if (status.equals("opposée")) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Card already opposed.");
                }
                carteService.opposeCardByCardType(manager.getUserId(), manager.getCarte().getTypeCarte(), manager.getRaisonsOpposition());
                return ResponseEntity.status(HttpStatus.CREATED).body("Card opposed successfully.");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Specify card number.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Card not found.");
        }
    }

}
