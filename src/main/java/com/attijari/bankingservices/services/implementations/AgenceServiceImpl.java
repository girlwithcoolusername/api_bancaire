package com.attijari.bankingservices.services.implementations;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.attijari.bankingservices.models.*;
import com.attijari.bankingservices.repositories.AgenceRepository;
import com.attijari.bankingservices.repositories.CompteRepository;
import com.attijari.bankingservices.repositories.UtilisateurRepository;
import com.attijari.bankingservices.services.AgenceService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

@Service
public class AgenceServiceImpl implements AgenceService {

    private static final double MAX_DISTANCE = 10000.0;
    private final String apiKey = "AIzaSyDRiSfn0djzWOIwneu-xLtuTgEZ-y7b_TM";

    private final AgenceRepository agenceRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final CompteRepository compteRepository;

    public AgenceServiceImpl(AgenceRepository agenceRepository, UtilisateurRepository utilisateurRepository, CompteRepository compteRepository) {
        this.agenceRepository = agenceRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.compteRepository = compteRepository;
    }

    @Override
    public List<Agence> getAllAgencies() {
        return agenceRepository.findAll();
    }

    @Override
    public List<Agence> getAgenciesByUserId(Long userId) {
        List<Agence> agencies = new ArrayList<>();

        Utilisateur utilisateur = utilisateurRepository.findById(userId).orElse(null);
        if (utilisateur != null) {
            Client client = utilisateur.getClient();
            if (client != null) {
                Long clientId = client.getIdClient();
                List<Compte> accounts = compteRepository.findByClientId(clientId);
                for (Compte account : accounts) {
                    Agence agence = account.getAgence();
                    if (agence != null) {
                        agencies.add(agence);
                    }
                }
            }
        }
        return agencies;
    }
    @Override
    public List<Agence> getAgenciesByRegistredAddress(Long userId) {
        Utilisateur user = utilisateurRepository.findById(userId).orElse(null);
        if (user == null) {
            return Collections.emptyList();
        }

        String address = user.getClient().getAdresse();

        try {
            URL url = new URL("https://maps.googleapis.com/maps/api/geocode/json?address=" + address + "&key=" + apiKey);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode response = mapper.readTree(url);

            JsonNode location = response.get("results").get(0).get("geometry").get("location");
            BigDecimal latitude = BigDecimal.valueOf(location.get("lat").asDouble());
            BigDecimal longitude = BigDecimal.valueOf(location.get("lng").asDouble());

            return getAgenciesByUserLocation(latitude, longitude);
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @Override
    public List<Agence> getAgenciesByUserLocation(BigDecimal lat, BigDecimal lng) {
        List<Agence> nearestAgencies = new ArrayList<>();

        for (Agence agence : agenceRepository.findAll()) {
            try {
                URL url = new URL("https://maps.googleapis.com/maps/api/geocode/json?address=" + agence.getAdresse() + "&key=" + apiKey);
                ObjectMapper mapper = new ObjectMapper();
                JsonNode response = mapper.readTree(url);

                JsonNode location = response.get("results").get(0).get("geometry").get("location");
                BigDecimal latitude = BigDecimal.valueOf(location.get("lat").asDouble());
                BigDecimal longitude = BigDecimal.valueOf(location.get("lng").asDouble());

                double latDiff = lat.doubleValue() - latitude.doubleValue();
                double lngDiff = lng.doubleValue() - longitude.doubleValue();

                double distance = Math.sqrt(Math.pow(latDiff, 2) + Math.pow(lngDiff, 2));
                if (distance <= MAX_DISTANCE) {
                    nearestAgencies.add(agence);
                }
            } catch (IOException e) {
                e.printStackTrace();
                return Collections.emptyList();
            }


        }

        return nearestAgencies;
    }
}
