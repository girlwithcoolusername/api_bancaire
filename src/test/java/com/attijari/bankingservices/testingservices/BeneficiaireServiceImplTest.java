package com.attijari.bankingservices.testingservices;

import com.attijari.bankingservices.models.Beneficiaire;
import com.attijari.bankingservices.models.Client;
import com.attijari.bankingservices.models.Utilisateur;
import com.attijari.bankingservices.repositories.BeneficiaireRepository;
import com.attijari.bankingservices.repositories.UtilisateurRepository;
import com.attijari.bankingservices.services.implementations.BeneficiaireServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BeneficiaireServiceImplTest {

    @Mock
    private BeneficiaireRepository beneficiaireRepository;
    @Mock
    private UtilisateurRepository utilisateurRepository;

    @InjectMocks
    private BeneficiaireServiceImpl beneficiaireService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testAddBeneficiary_Success() {
        // Mock data
        Long userId = 1L;
        String rib = "123456789";
        String firstname = "John";
        String lastname = "Doe";
        String type = "TYPE";

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setClient(new Client());
        when(utilisateurRepository.findById(userId)).thenReturn(Optional.of(utilisateur));

        // Call the method
        beneficiaireService.addBeneficiary(userId, rib, firstname, lastname, type);

        // Verify the result
        verify(beneficiaireRepository, times(1)).save(any());
    }

    @Test
    void testGetBeneficiaryByUserIdNames_Success() {
        // Mock data
        Long userId = 1L;
        String firstname = "John";
        String lastname = "Doe";
        List<Beneficiaire> beneficiaries = new ArrayList<>();
        beneficiaries.add(new Beneficiaire());

        Client client = new Client();
        client.setIdClient(1L);
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setClient(client);
        when(utilisateurRepository.findById(userId)).thenReturn(Optional.of(utilisateur));
        when(beneficiaireRepository.findByClientIdAndNames(client.getIdClient(), firstname, lastname)).thenReturn(beneficiaries);

        // Call the method
        List<Beneficiaire> result = beneficiaireService.getBeneficiaryByUserIdNames(userId, firstname, lastname);

        // Verify the result
        assertEquals(beneficiaries, result);
    }

    // Test other methods in a similar manner...

    @Test
    void testDeleteBeneficiaryByRib_Success() {
        // Mock data
        Long userId = 1L;
        String rib = "123456789";

        Client client = new Client();
        client.setIdClient(1L);
        when(utilisateurRepository.findById(userId)).thenReturn(Optional.of(new Utilisateur()));
        when(beneficiaireRepository.findByClientIdAndRib(client.getIdClient(), rib)).thenReturn(new Beneficiaire());

        // Call the method
        beneficiaireService.deleteBeneficiaryByRib(userId, rib);

        // Verify the result
        verify(beneficiaireRepository, times(1)).delete(any());
    }

    @Test
    void testGetBeneficiaryByUserIdAndRib_Success() {
        // Mock data
        Long userId = 1L;
        String rib = "123456789";

        Client client = new Client();
        client.setIdClient(1L);
        when(utilisateurRepository.findById(userId)).thenReturn(Optional.of(new Utilisateur()));
        when(beneficiaireRepository.findByClientIdAndRib(client.getIdClient(), rib)).thenReturn(new Beneficiaire());

        // Call the method
        Beneficiaire result = beneficiaireService.getBeneficiaryByUserIdAndRib(userId, rib);

        // Verify the result
        assertNotNull(result);
    }
}
