package com.attijari.bankingservices.utils;

import lombok.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Optional;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ManageUserCardLimits {
    Long userId;
    Long numeroCarte;
    String typeCarte;
    BigDecimal plafond;
    String typePlafond;
    Optional<Timestamp> duration;
    String statut;

}



