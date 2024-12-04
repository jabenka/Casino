package org.zxcjaba.casino.ControllersTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.zxcjaba.casino.api.controller.UserController;
import org.zxcjaba.casino.api.dto.UserDto;
import org.zxcjaba.casino.persistence.entity.UserEntity;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private Authentication authentication;

    @Mock
    private UserEntity mockUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        UserController userController = new UserController();
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        ObjectMapper objectMapper = new ObjectMapper();
    }

    @Test
    void testAuthenticatedUser() throws Exception {
        when(authentication.getPrincipal()).thenReturn(mockUser);
        when(mockUser.getId()).thenReturn(1L);
        when(mockUser.getName()).thenReturn("John");
        when(mockUser.getSurname()).thenReturn("Doe");
        when(mockUser.getEmail()).thenReturn("john.doe@example.com");
        when(mockUser.getPassword()).thenReturn("password");
        when(mockUser.getBalance()).thenReturn(new BigDecimal("1000.0"));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDto expectedUserDto = UserDto.builder()
                .id(1L)
                .name("John")
                .surname("Doe")
                .email("john.doe@example.com")
                .password("password")
                .balance(new BigDecimal("1000.0"))
                .build();

        mockMvc.perform(get("/user/me")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.surname").value("Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.balance").value("1000.0"));

        verify(authentication, times(1)).getPrincipal();
    }

    @Test
    void testGetBalance() throws Exception {
        when(authentication.getPrincipal()).thenReturn(mockUser);
        when(mockUser.getBalance()).thenReturn(new BigDecimal("1000.00"));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        mockMvc.perform(get("/user/balance")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("1000.00"));

        verify(authentication, times(1)).getPrincipal();
    }
}
