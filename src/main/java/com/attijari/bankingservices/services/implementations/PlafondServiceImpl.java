package com.attijari.bankingservices.services.implementations;

import com.attijari.bankingservices.models.Carte;
import com.attijari.bankingservices.models.Plafond;
import com.attijari.bankingservices.repositories.CarteRepository;
import com.attijari.bankingservices.repositories.FactureRepository;
import com.attijari.bankingservices.repositories.PlafondRepository;
import com.attijari.bankingservices.services.PlafondService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
public class PlafondServiceImpl implements PlafondService {

    private final PlafondRepository plafondRepository;
    private final CarteRepository carteRepository;
    private final FactureRepository factureRepository;

    @Autowired
    public PlafondServiceImpl(PlafondRepository plafondRepository,
                              CarteRepository carteRepository,
                              FactureRepository factureRepository) {
        this.plafondRepository = plafondRepository;
        this.carteRepository = carteRepository;
        this.factureRepository = factureRepository;
    }

    @Override
    public void updateCardLimitByCardNum(Long cardNum, BigDecimal limit, String typeLimit, Optional<Timestamp> duration,String status) {
        Optional<Carte> card = carteRepository.findByNumeroCarte(cardNum);
        if (card.isPresent()) {
            Carte carte = card.get();
            List<Plafond> existingPlafond = plafondRepository.findByCarte(carte);
            if (!existingPlafond.isEmpty()) {
                for (Plafond plafond : existingPlafond) {
                    if (plafond.getTypePlafond().equals(typeLimit)) {
                        BigDecimal initialLimit = plafond.getMontantPlafond();
                        plafond.setTypePlafond(typeLimit);
                        if(status.equals("add"))
                        {
                            BigDecimal new_limit = limit.add(initialLimit);
                            plafond.setMontantPlafond(new_limit);
                        }
                        else{
                            BigDecimal new_limit = limit.subtract(initialLimit);
                            plafond.setMontantPlafond(new_limit);
                        }
                        plafond.setPeriode("Par jour");
                        plafond.setDateDebut(new Timestamp(System.currentTimeMillis()));
                        plafond.setCarte(carte);
                        if (duration.isEmpty()) {
                            plafond.setEstPermanent(true);
                            plafond.setDateExpiration(null);
                        } else {
                            plafond.setEstPermanent(false);
                            plafond.setDateExpiration(duration.get());
                        }
                        plafondRepository.save(plafond);
                    } else {
                        Plafond newPlafond = new Plafond();
                        newPlafond.setTypePlafond(typeLimit);
                        newPlafond.setMontantPlafond(limit);
                        newPlafond.setPeriode("Par jour");
                        newPlafond.setDateDebut(new Timestamp(System.currentTimeMillis()));
                        newPlafond.setCarte(carte);
                        if (duration.isEmpty()) {
                            newPlafond.setEstPermanent(true);
                        } else {
                            newPlafond.setEstPermanent(false);
                            newPlafond.setDateExpiration(duration.get());
                        }
                        plafondRepository.save(newPlafond);
                    }
                }

            } else {
                Plafond newPlafond = new Plafond();
                newPlafond.setTypePlafond(typeLimit);
                newPlafond.setMontantPlafond(limit);
                newPlafond.setPeriode("Par jour");
                newPlafond.setDateDebut(new Timestamp(System.currentTimeMillis()));
                newPlafond.setCarte(carte);
                if (duration.isEmpty()) {
                    newPlafond.setEstPermanent(true);
                } else {
                    newPlafond.setEstPermanent(false);
                    newPlafond.setDateExpiration(duration.get());
                }
                plafondRepository.save(newPlafond);
            }
        } else {
            System.out.println("Carte non retrouvée");
        }
    }


    @Override
    public void updateCardLimitByCardType(Long userID, String cardType, BigDecimal limit, String typeLimit, Optional<Timestamp> duration,String status) {
        List<Carte> cartes = carteRepository.findByTypeCarte(cardType);
        Carte carte = cartes.get(0);
        if (carte != null) {
            List<Plafond> existingPlafond = plafondRepository.findByCarte(carte);
            if (!existingPlafond.isEmpty()) {
                for (Plafond plafond : existingPlafond) {
                    if (plafond.getTypePlafond().equals(typeLimit)) {
                        BigDecimal initialLimit = plafond.getMontantPlafond();
                        plafond.setTypePlafond(typeLimit);
                        if(status.equals("add"))
                        {
                            BigDecimal new_limit = limit.add(initialLimit);
                            plafond.setMontantPlafond(new_limit);
                        }
                        else{
                            BigDecimal new_limit = limit.subtract(initialLimit);
                            plafond.setMontantPlafond(new_limit);
                        }                        plafond.setPeriode("Par jour");
                        plafond.setDateDebut(new Timestamp(System.currentTimeMillis()));
                        plafond.setCarte(carte);
                        if (duration.isEmpty()) {
                            plafond.setEstPermanent(true);
                            plafond.setDateExpiration(null);
                        } else {
                            plafond.setEstPermanent(false);
                            plafond.setDateExpiration(duration.get());
                        }
                        plafondRepository.save(plafond);
                    } else {
                        Plafond newPlafond = new Plafond();
                        newPlafond.setTypePlafond(typeLimit);
                        newPlafond.setMontantPlafond(limit);
                        newPlafond.setPeriode("Par jour");
                        newPlafond.setDateDebut(new Timestamp(System.currentTimeMillis()));
                        newPlafond.setCarte(carte);
                        if (duration.isEmpty()) {
                            newPlafond.setEstPermanent(true);
                        } else {
                            newPlafond.setEstPermanent(false);
                            newPlafond.setDateExpiration(duration.get());
                        }
                        plafondRepository.save(newPlafond);
                    }
                }

            } else {
                Plafond newPlafond = new Plafond();
                newPlafond.setTypePlafond(typeLimit);
                newPlafond.setMontantPlafond(limit);
                newPlafond.setPeriode("Par jour");
                newPlafond.setDateDebut(new Timestamp(System.currentTimeMillis()));
                newPlafond.setCarte(carte);
                if (duration.isEmpty()) {
                    newPlafond.setEstPermanent(true);
                } else {
                    newPlafond.setEstPermanent(false);
                    newPlafond.setDateExpiration(duration.get());
                }
                plafondRepository.save(newPlafond);
            }
        } else {
            System.out.println("Carte non retrouvée");
        }
    }
}
