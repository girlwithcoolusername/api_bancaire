package com.attijari.bankingservices.testingservices;

import com.attijari.bankingservices.models.Client;
import com.attijari.bankingservices.models.Compte;
import com.attijari.bankingservices.models.Utilisateur;
import com.attijari.bankingservices.repositories.CompteRepository;
import com.attijari.bankingservices.repositories.UtilisateurRepository;
import com.attijari.bankingservices.services.implementations.CompteServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CompteServiceImplTest {

    @Mock
    private UtilisateurRepository utilisateurRepository;

    @Mock
    private CompteRepository compteRepository;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private CompteServiceImpl compteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetAccountByUserID() {
        // Mock data
        Long userId = 1L;
        Utilisateur user = new Utilisateur();
        user.setIdUser(userId);
        Client client = new Client();
        client.setIdClient(userId);
        List<Compte> expectedComptes = new ArrayList<>();
        Compte compte1 = new Compte();
        compte1.setNumeroCompte("123");
        compte1.setTypeCompte("Checking");
        expectedComptes.add(compte1);

        when(utilisateurRepository.findById(userId)).thenReturn(Optional.of(user));
        when(compteRepository.findByClientId(user.getClient().getIdClient())).thenReturn(expectedComptes);

        // Call the method
        List<Compte> result = compteService.getAccountByUserID(userId);

        // Verify the result
        assertEquals(expectedComptes, result);
    }

    @Test
    void testGetAccountByUserIdAndAccountType() {
        // Mock data
        Long userId = 1L;
        String accountType = "Checking";
        Compte compte1 = new Compte();
        compte1.setNumeroCompte("123");
        compte1.setTypeCompte("Checking");
        Compte compte2 = new Compte();
        compte2.setNumeroCompte("456");
        compte2.setTypeCompte("Savings");
        List<Compte> comptes = Arrays.asList(compte1, compte2);
        List<Compte> expectedComptes = Collections.singletonList(compte1);

        when(compteRepository.findByClientId(any())).thenReturn(comptes);

        // Call the method
        List<Compte> result = compteService.getAccountByUserIdAndAccountType(userId, accountType);

        // Verify the result
        assertEquals(expectedComptes, result);
    }

    @Test
    void testGetAccountByUserIdAndAccountNum() {
        // Mock data
        Long userId = 1L;
        String accountNum = "123";
        Compte compte1 = new Compte();
        compte1.setNumeroCompte("123");
        compte1.setTypeCompte("Checking");
        Compte compte2 = new Compte();
        compte2.setNumeroCompte("456");
        compte2.setTypeCompte("Savings");
        List<Compte> comptes = Arrays.asList(compte1, compte2);
        Optional<Compte> expectedCompte = Optional.of(compte1);

        when(compteRepository.findByClientId(any())).thenReturn(comptes);

        // Call the method
        Optional<Compte> result = compteService.getAccountByUserIdAndAccountNum(userId, accountNum);

        // Verify the result
        assertEquals(expectedCompte, result);
    }

    @Test
    void testGetAccountsByUserIdAndEntities() {
        // Mock data
        Long userId = 1L;
        Utilisateur user = new Utilisateur();
        user.setIdUser(userId);
        Map<String, Object> entitiesDict = new HashMap<>();
        entitiesDict.put("type", "Checking");
        entitiesDict.put("balance", new BigDecimal("1000.00"));
        Client client = new Client();
        client.setIdClient(userId);
        List<Compte> expectedComptes = new ArrayList<>();
        Compte compte1 = new Compte();
        compte1.setNumeroCompte("123");
        compte1.setTypeCompte("Checking");
        compte1.setSolde(new BigDecimal("1000.00"));
        expectedComptes.add(compte1);

        when(utilisateurRepository.findById(userId)).thenReturn(Optional.of(user));
        when(compteRepository.findByClientIdAndEntities(userId, entitiesDict, entityManager)).thenReturn(expectedComptes);

        // Call the method
        List<Compte> result = compteService.getAccountsByUserIdAndEntities(userId, entitiesDict);

        // Verify the result
        assertEquals(expectedComptes, result);
    }

    @Test
    void testGetBalanceByUserIdAndEntities() {
        // Mock data
        Long userId = 1L;
        Utilisateur user = new Utilisateur();
        user.setIdUser(userId);
        Map<String, Object> entitiesDict = new HashMap<>();
        entitiesDict.put("type", "Checking");
        entitiesDict.put("balance", new BigDecimal("1000.00"));
        Client client = new Client();
        client.setIdClient(userId);
        List<Compte> comptes = new ArrayList<>();
        Compte compte1 = new Compte();
        compte1.setNumeroCompte("123");
        compte1.setTypeCompte("Checking");
        compte1.setSolde(new BigDecimal("1000.00"));
        comptes.add(compte1);
        Map<BigDecimal, Compte> expectedBalance = new HashMap<>();
        expectedBalance.put(new BigDecimal("1000.00"), compte1);

        when(utilisateurRepository.findById(userId)).thenReturn(Optional.of(user));
        when(compteRepository.findByClientIdAndEntities(userId, entitiesDict, entityManager)).thenReturn(comptes);

        // Call the method
        Map<BigDecimal, Compte> result = compteService.getBalanceByUserIdAndEntities(userId, entitiesDict);

        // Verify the result
        assertEquals(expectedBalance, result);
    }

    @Test
    void testGetAccountUserId() {
        // Mock data
        Long idCompte = 1L;
        Compte compte = new Compte();
        compte.setIdCompte(idCompte);
        Client client = new Client();
        client.setIdClient(2L);
        Utilisateur user1 = new Utilisateur();
        user1.setIdUser(1L);
        user1.setClient(client);
        Utilisateur user2 = new Utilisateur();
        user2.setIdUser(2L);
        List<Utilisateur> users = Arrays.asList(user1, user2);

        when(compteRepository.findById(idCompte)).thenReturn(Optional.of(compte));
        when(utilisateurRepository.findAll()).thenReturn(users);

        // Call the method
        Long result = compteService.getAccountUserId(idCompte);

        // Verify the result
        assertEquals(user1.getIdUser(), result);
    }
}
