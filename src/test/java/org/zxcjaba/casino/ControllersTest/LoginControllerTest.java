package org.zxcjaba.casino.ControllersTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.zxcjaba.casino.api.Controllers.LoginController;
import org.zxcjaba.casino.api.DTO.LoginDto;
import org.zxcjaba.casino.api.DTO.LoginResponse;
import org.zxcjaba.casino.api.DTO.RegistrationDto;
import org.zxcjaba.casino.api.DTO.UserDto;
import org.zxcjaba.casino.api.Services.AuthenticationService;
import org.zxcjaba.casino.api.Services.JwtService;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class LoginControllerTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private LoginController loginController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(loginController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testSignup() throws Exception {
        RegistrationDto registrationDto = new RegistrationDto();
        registrationDto.setEmail("user@example.com");
        registrationDto.setPassword("password");
        registrationDto.setName("John");
        registrationDto.setSurname("Doe");

        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setEmail("user@example.com");
        userDto.setName("John");
        userDto.setSurname("Doe");
        userDto.setBalance(new BigDecimal("1000.00"));

        when(authenticationService.signUp(any(RegistrationDto.class))).thenReturn(userDto);

        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registrationDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.email").value("user@example.com"))
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.surname").value("Doe"))
                .andExpect(jsonPath("$.balance").value("1000.0"));

        verify(authenticationService, times(1)).signUp(any(RegistrationDto.class));
    }

    @Test
    void testLogin() throws Exception {
        LoginDto loginDto = new LoginDto();
        loginDto.setEmail("user@example.com");
        loginDto.setPassword("password");

        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setEmail("user@example.com");
        userDto.setName("John");
        userDto.setSurname("Doe");
        userDto.setPassword("password");
        userDto.setBalance(new BigDecimal("1000.00"));

        String token = "fake-jwt-token";
        long expirationTime = 3600L;

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(token);
        loginResponse.setExpirationTime(expirationTime);

        when(authenticationService.authenticate(any(LoginDto.class))).thenReturn(userDto);
        when(jwtService.generateToken(any())).thenReturn(token);
        when(jwtService.getExpirationTime()).thenReturn(expirationTime);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(token))
                .andExpect(jsonPath("$.expirationTime").value(expirationTime));

        verify(authenticationService, times(1)).authenticate(any(LoginDto.class));
        verify(jwtService, times(1)).generateToken(any());
        verify(jwtService, times(1)).getExpirationTime();
    }

    @Test
    void testLoginInvalidCredentials() throws Exception {
        LoginDto loginDto = new LoginDto();
        loginDto.setEmail("user@example.com");
        loginDto.setPassword("wrongpassword");

        when(authenticationService.authenticate(any(LoginDto.class))).thenThrow(new RuntimeException("Invalid credentials"));

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isUnauthorized());

        verify(authenticationService, times(1)).authenticate(any(LoginDto.class));
    }
}
