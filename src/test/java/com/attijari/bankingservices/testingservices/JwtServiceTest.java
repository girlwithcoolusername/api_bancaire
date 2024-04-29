package com.attijari.bankingservices.testingservices;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import com.attijari.bankingservices.models.AuthCredentials;
import com.attijari.bankingservices.models.Token;
import com.attijari.bankingservices.repositories.TokenRepository;
import com.attijari.bankingservices.services.implementations.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Optional;

public class JwtServiceTest {

    private final String SECRET_KEY = "4bb6d1dfbafb64a681139d1586b6f1160d18159afd57c8c79136d7490630407c";

    private JwtService jwtService;
    private TokenRepository tokenRepository;

    @BeforeEach
    void setUp() {
        tokenRepository = mock(TokenRepository.class);
        jwtService = new JwtService(tokenRepository);
    }

    @Test
    void testExtractUsername() {
        // Mock token
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiJ9.vJ-_HXfw9JYl0kOInYrc51I5_2TPgLlYHRoYJl1-_vM";

        // Call the method
        String username = jwtService.extractUsername(token);

        // Verify
        assertEquals("admin", username);
    }

    @Test
    void testExtractClaim() {
        // Mock token
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiJ9.vJ-_HXfw9JYl0kOInYrc51I5_2TPgLlYHRoYJl1-_vM";

        // Call the method
        String subject = jwtService.extractClaim(token, Claims::getSubject);

        // Verify
        assertEquals("admin", subject);
    }

    @Test
    void testGenerateToken() {
        // Mock auth credentials
        AuthCredentials authCredentials = new AuthCredentials();
        authCredentials.setUsername("admin");

        // Call the method
        String token = jwtService.generateToken(authCredentials);

        // Verify
        assertNotNull(token);
    }
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(getSigninKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSigninKey() {
        byte[] keyBytes = Decoders.BASE64URL.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    private Date extractExpiration(String token) {
        return jwtService.extractClaim(token, Claims::getExpiration);
    }
    @Test
    void testIsTokenExpired() {
        // Mock token
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiJ9.vJ-_HXfw9JYl0kOInYrc51I5_2TPgLlYHRoYJl1-_vM";

        // Call the method
        boolean isExpired = isTokenExpired(token);

        // Verify
        assertFalse(isExpired);
    }

    @Test
    void testIsValid() {
        // Mock user details
        UserDetails user = new User("admin", "password", null);

        // Mock token
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiJ9.vJ-_HXfw9JYl0kOInYrc51I5_2TPgLlYHRoYJl1-_vM";

        // Mock token repository
        when(tokenRepository.findByToken(token)).thenReturn(Optional.of(new Token()));

        // Call the method
        boolean isValid = jwtService.isValid(token, user);

        // Verify
        assertTrue(isValid);
    }

    @Test
    void testIsValidTokenExpired() {
        // Mock user details
        UserDetails user = new User("admin", "password", null);

        // Mock token
        String expiredToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiJ9.vJ-_HXfw9JYl0kOInYrc51I5_2TPgLlYHRoYJl1-_vM";
        Date expiredDate = new Date(System.currentTimeMillis() - 1000);
        when(tokenRepository.findByToken(expiredToken)).thenReturn(Optional.of(new Token()));
        when(extractExpiration(expiredToken)).thenReturn(expiredDate);

        // Call the method
        boolean isValid = jwtService.isValid(expiredToken, user);

        // Verify
        assertFalse(isValid);
    }

    @Test
    void testIsValidTokenLoggedOut() {
        // Mock user details
        UserDetails user = new User("admin", "password", null);

        // Mock token
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiJ9.vJ-_HXfw9JYl0kOInYrc51I5_2TPgLlYHRoYJl1-_vM";
        Token loggedOutToken = new Token();
        loggedOutToken.setLoggedOut(true);
        when(tokenRepository.findByToken(token)).thenReturn(Optional.of(loggedOutToken));

        // Call the method
        boolean isValid = jwtService.isValid(token, user);

        // Verify
        assertFalse(isValid);
    }


    @Test
    void testIsValidIncorrectResultSize() {
        // Mock user details
        UserDetails user = new User("admin", "password", null);

        // Mock token
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiJ9.vJ-_HXfw9JYl0kOInYrc51I5_2TPgLlYHRoYJl1-_vM";
        when(tokenRepository.findByToken(token)).thenThrow(IncorrectResultSizeDataAccessException.class);

        // Call the method
        boolean isValid = jwtService.isValid(token, user);

        // Verify
        assertFalse(isValid);
    }
}
