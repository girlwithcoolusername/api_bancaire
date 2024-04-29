package com.attijari.bankingservices.testingservices;

import com.attijari.bankingservices.models.Carte;
import com.attijari.bankingservices.models.Client;
import com.attijari.bankingservices.models.Compte;
import com.attijari.bankingservices.models.Utilisateur;
import com.attijari.bankingservices.repositories.CarteRepository;
import com.attijari.bankingservices.repositories.CompteRepository;
import com.attijari.bankingservices.repositories.UtilisateurRepository;
import com.attijari.bankingservices.services.implementations.CarteServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CarteServiceImplTest {

    @Mock
    private UtilisateurRepository utilisateurRepository;
    @Mock
    private CompteRepository compteRepository;
    @Mock
    private CarteRepository carteRepository;

    @InjectMocks
    private CarteServiceImpl carteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetCardByUserId_Success() {
        // Mock data
        Long userId = 1L;
        List<Compte> comptes = new ArrayList<>();
        comptes.add(new Compte());
        List<Carte> cartes = new ArrayList<>();
        cartes.add(new Carte());

        Utilisateur utilisateur = new Utilisateur();
        Client client = new Client();
        client.setIdClient(1L);
        utilisateur.setClient(client);
        when(utilisateurRepository.findById(userId)).thenReturn(Optional.of(utilisateur));
        when(compteRepository.findByClientId(client.getIdClient())).thenReturn(comptes);
        when(carteRepository.findAll()).thenReturn(cartes);

        // Call the method
        List<Carte> result = carteService.getCardByUserId(userId);

        // Verify the result
        assertEquals(cartes, result);
    }

    @Test
    void testGetCardByUserId_UserNotFound() {
        // Mock data
        Long userId = 1L;

        when(utilisateurRepository.findById(userId)).thenReturn(Optional.empty());

        // Call the method and verify exception
        assertThrows(IllegalArgumentException.class, () -> carteService.getCardByUserId(userId));
    }

    @Test
    void testGetCardByNum_Success() {
        // Mock data
        Long cardNum = 123456789L;
        Carte carte = new Carte();

        when(carteRepository.findByNumeroCarte(cardNum)).thenReturn(Optional.of(carte));

        // Call the method
        Optional<Carte> result = carteService.getCardByNum(cardNum);

        // Verify the result
        assertTrue(result.isPresent());
        assertEquals(carte, result.get());
    }

    @Test
    void testGetCardByUserIdAndType_Success() {
        // Mock data
        Long userId = 1L;
        String typeCard = "debit";
        List<Compte> comptes = new ArrayList<>();
        Compte compte = new Compte();
        compte.setIdCompte(1L);
        comptes.add(compte);
        List<Carte> cartes = new ArrayList<>();
        Carte carte = new Carte();
        carte.setCompte(compte);
        carte.setTypeCarte(typeCard);
        cartes.add(carte);

        Utilisateur utilisateur = new Utilisateur();
        Client client = new Client();
        client.setIdClient(1L);
        utilisateur.setClient(client);
        when(utilisateurRepository.findById(userId)).thenReturn(Optional.of(utilisateur));
        when(compteRepository.findByClientId(client.getIdClient())).thenReturn(comptes);

        // Call the method
        List<Carte> result = carteService.getCardByUserIdAndType(userId, typeCard);

        // Verify the result
        assertEquals(cartes, result);
    }

    @Test
    void testGetCardByUserIdAndEntities_Success() {
        // Mock data
        Long userId = 1L;
        Map<String, Object> entitiesDict = new HashMap<>();
        entitiesDict.put("typeCarte", "debit");
        entitiesDict.put("services", "online");

        List<Carte> cartes = new ArrayList<>();
        Carte carte = new Carte();
        carte.setTypeCarte("debit");
        carte.setServices("online");
        cartes.add(carte);

        when(carteRepository.findAll()).thenReturn(cartes);

        // Call the method
        List<Carte> result = carteService.getCardByUserIdAndEntities(userId, entitiesDict);

        // Verify the result
        assertEquals(cartes, result);
    }

    @Test
    void testUpdateCardServicesByCardNum_Enable() {
        // Mock data
        Long userId = 1L;
        Long cardNum = 123456789L;
        List<String> services = Arrays.asList("service1", "service2");
        String status = "enable";

        List<Carte> cartes = new ArrayList<>();
        Carte carte = new Carte();
        carte.setNumeroCarte(cardNum);
        carte.setServices("service1");
        cartes.add(carte);

        when(carteRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(carteRepository.findAll()).thenReturn(cartes);

        // Call the method
        carteService.updateCardServicesByCardNum(userId, cardNum, services, status);

        // Verify the result
        assertEquals("service1,service2", carte.getServices());
        verify(carteRepository, times(1)).save(any());
    }

    @Test
    void testUpdateCardServicesByCardType_Disable() {
        // Mock data
        Long userId = 1L;
        String cardType = "debit";
        List<String> services = Collections.singletonList("service1");
        String status = "disable";

        List<Carte> cartes = new ArrayList<>();
        Carte carte = new Carte();
        carte.setTypeCarte(cardType);
        carte.setServices("service1");
        cartes.add(carte);

        when(carteRepository.findAll()).thenReturn(cartes);

        // Call the method
        carteService.updateCardServicesByCardType(userId, cardType, services, status);

        // Verify the result
        assertEquals("", carte.getServices());
        verify(carteRepository, times(1)).save(any());
    }

    @Test
    void testOpposeCardByCardNum_Success() {
        // Mock data
        Long userId = 1L;
        Long cardNum = 123456789L;
        String reasons = "Lost card";

        List<Carte> cartes = new ArrayList<>();
        Carte carte = new Carte();
        carte.setNumeroCarte(cardNum);
        cartes.add(carte);

        when(carteRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(carteRepository.findAll()).thenReturn(cartes);

        // Call the method
        carteService.opposeCardByCardNum(userId, cardNum, reasons);

        // Verify the result
        assertEquals("opposée", carte.getStatutCarte());
        assertEquals(reasons, carte.getRaisonsOpposition());
        assertNotNull(carte.getDateOpposition());
        verify(carteRepository, times(1)).save(any());
    }

    @Test
    void testOpposeCardByCardType_Success() {
        // Mock data
        Long userId = 1L;
        String cardType = "debit";
        String reasons = "Lost card";

        List<Carte> cartes = new ArrayList<>();
        Carte carte = new Carte();
        carte.setTypeCarte(cardType);
        cartes.add(carte);

        when(carteRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(carteRepository.findAll()).thenReturn(cartes);

        // Call the method
        carteService.opposeCardByCardType(userId, cardType, reasons);

        // Verify the result
        assertEquals("opposée", carte.getStatutCarte());
        assertEquals(reasons, carte.getRaisonsOpposition());
        assertNotNull(carte.getDateOpposition());
        verify(carteRepository, times(1)).save(any());
    }
}
