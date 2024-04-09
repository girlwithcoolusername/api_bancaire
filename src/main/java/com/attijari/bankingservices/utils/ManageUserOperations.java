package com.attijari.bankingservices.utils;

import com.attijari.bankingservices.models.Operation;
import lombok.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Optional;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ManageUserOperations {
    Long userId;
    String typeCompte;
    String type;
    BigDecimal montant;
    String numeroCompte;
    String ribBeneficiaire;
    Optional<String> motif;
}