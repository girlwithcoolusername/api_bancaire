package com.attijari.bankingservices.testingservices;

import com.attijari.bankingservices.models.Beneficiaire;
import com.attijari.bankingservices.models.Client;
import com.attijari.bankingservices.models.Facture;
import com.attijari.bankingservices.models.Utilisateur;
import com.attijari.bankingservices.repositories.FactureRepository;
import com.attijari.bankingservices.repositories.UtilisateurRepository;
import com.attijari.bankingservices.services.implementations.FactureServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FactureServiceImplTest {

    @Mock
    private FactureRepository factureRepository;

    @Mock
    private UtilisateurRepository utilisateurRepository;

    @InjectMocks
    private FactureServiceImpl factureService;

    @Test
    void testUpdateInvoiceStatus() {
        // Mock data
        Long userId = 1L;
        Utilisateur user = new Utilisateur();
        user.setIdUser(userId);
        Long beneficiaryId = 2L;
        String status = "paid";

        Facture invoice = new Facture();
        invoice.setIdFacture(1L);
        invoice.setStatutPaiement("pending");
        Client client = new Client();
        client.setIdClient(userId);
        invoice.setClient(client);
        invoice.setBeneficiaire(new Beneficiaire());
        List<Facture> invoiceList = Arrays.asList(invoice);

        when(utilisateurRepository.findById(userId)).thenReturn(Optional.of(user));
        when(factureRepository.findAll()).thenReturn(invoiceList);

        // Call the method
        factureService.updateInvoiceStatus(userId, beneficiaryId, status);

        // Verify the result
        assertEquals(status, invoice.getStatutPaiement());
        verify(factureRepository, times(1)).save(invoice);
    }

    @Test
    void testCheckInvoiceStatus() {
        // Mock data
        String numFacture = "123";
        Facture invoice = new Facture();
        invoice.setNumeroFacture(numFacture);
        invoice.setStatutPaiement("paid");

        when(factureRepository.findByNumeroFacture(numFacture)).thenReturn(Optional.of(invoice));

        // Call the method
        boolean result = factureService.checkInvoiceStatus(numFacture);

        // Verify the result
        assertTrue(result);
    }

    @Test
    void testCheckInvoiceStatus_NotPaid() {
        // Mock data
        String numFacture = "123";
        Facture invoice = new Facture();
        invoice.setNumeroFacture(numFacture);
        invoice.setStatutPaiement("pending");

        when(factureRepository.findByNumeroFacture(numFacture)).thenReturn(Optional.of(invoice));

        // Call the method
        boolean result = factureService.checkInvoiceStatus(numFacture);

        // Verify the result
        assertFalse(result);
    }

    @Test
    void testCheckInvoiceStatus_NotFound() {
        // Mock data
        String numFacture = "123";

        when(factureRepository.findByNumeroFacture(numFacture)).thenReturn(Optional.empty());

        // Call the method
        boolean result = factureService.checkInvoiceStatus(numFacture);

        // Verify the result
        assertFalse(result);
    }

    @Test
    void testGetInvoiceByNumber() {
        // Mock data
        String numeroFacture = "123";
        Facture invoice = new Facture();
        invoice.setNumeroFacture(numeroFacture);

        when(factureRepository.findByNumeroFacture(numeroFacture)).thenReturn(Optional.of(invoice));

        // Call the method
        Optional<Facture> result = factureService.getInvoiceByNumber(numeroFacture);

        // Verify the result
        assertTrue(result.isPresent());
        assertEquals(invoice, result.get());
    }

    @Test
    void testGetInvoiceByNumber_NotFound() {
        // Mock data
        String numeroFacture = "123";

        when(factureRepository.findByNumeroFacture(numeroFacture)).thenReturn(Optional.empty());

        // Call the method
        Optional<Facture> result = factureService.getInvoiceByNumber(numeroFacture);

        // Verify the result
        assertFalse(result.isPresent());
    }
}
