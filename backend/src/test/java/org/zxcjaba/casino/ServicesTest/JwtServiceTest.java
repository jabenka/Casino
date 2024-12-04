package org.zxcjaba.casino.ServicesTest;

import io.jsonwebtoken.ExpiredJwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.zxcjaba.casino.api.service.JwtService;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class JwtServiceTest {

    private JwtService jwtService;

    @Mock
    private UserDetails userDetails;

    @BeforeEach
    public void setUp() {
        jwtService = new JwtService();
        jwtService.setSecret("3cfa76ef14937c1c0ea519f8fc057a80fcd04a7420f8e8bcd0a7567c272e007b324113wqffqf23f2dw3df23fi68t8i3rnb8623itr2386rt2876rf");
        jwtService.setExpirationTime(1000L * 60L * 60L);
    }

    @Test
    public void testGenerateToken() {
        UserDetails userDetails = new User("testuser", "password", new ArrayList<>());
        String token = jwtService.generateToken(userDetails);

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    public void testExtractUsernameFromToken() {
        String username = "testuser";
        UserDetails userDetails = new User(username, "password", new ArrayList<>());
        String token = jwtService.generateToken(userDetails);

        String extractedUsername = jwtService.extractUsername(token);

        assertEquals(username, extractedUsername);
    }

    @Test
    public void testIsTokenValid() {
        String username = "testuser";
        UserDetails userDetails = new User(username, "password", new ArrayList<>());
        String token = jwtService.generateToken(userDetails);

        boolean isValid = jwtService.isTokenValid(token, userDetails);

        assertTrue(isValid);
    }

    @Test
    public void testIsTokenExpired() {
        String username = "testuser";
        UserDetails userDetails = new User(username, "password", new ArrayList<>());
        String token = jwtService.generateToken(userDetails);

        boolean isExpired = jwtService.isTokenExpired(token);

        assertFalse(isExpired);
    }

    @Test
    public void testIsTokenExpiredAfterExpiration() {
        String username = "testuser";
        UserDetails userDetails = new User(username, "password", new ArrayList<>());

        jwtService.setExpirationTime(1000L);

        String token = jwtService.generateToken(userDetails);
        try {
            Thread.sleep(2000);
        }catch (InterruptedException e) {
            System.out.printf("InterruptedException: %s\n", e.getMessage());
        }
        boolean isExpired = false;
        try {
             isExpired = jwtService.isTokenExpired(token);
        }catch (ExpiredJwtException e) {
             isExpired = true;
        }

        assertTrue(isExpired);
    }
}
