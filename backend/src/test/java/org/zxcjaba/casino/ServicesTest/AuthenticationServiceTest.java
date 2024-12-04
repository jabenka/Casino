package org.zxcjaba.casino.ServicesTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.zxcjaba.casino.api.dto.LoginDto;
import org.zxcjaba.casino.api.dto.RegistrationDto;
import org.zxcjaba.casino.api.dto.UserDto;
import org.zxcjaba.casino.api.exception.UsernameException;
import org.zxcjaba.casino.api.service.AuthenticationService;
import org.zxcjaba.casino.persistence.entity.UserEntity;
import org.zxcjaba.casino.persistence.repository.UserRepository;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationService authenticationService;

    private UserEntity userEntity;
    private LoginDto loginDto;
    private RegistrationDto registrationDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setEmail("test@example.com");
        userEntity.setName("John");
        userEntity.setSurname("Doe");
        userEntity.setPassword("password");
        userEntity.setBalance(BigDecimal.valueOf(100));

        loginDto = new LoginDto();
        loginDto.setEmail("test@example.com");
        loginDto.setPassword("password");

        registrationDto = new RegistrationDto();
        registrationDto.setEmail("newuser@example.com");
        registrationDto.setName("Jane");
        registrationDto.setSurname("Doe");
        registrationDto.setPassword("newpassword");
    }

    @Test
    void authenticate_Success() {
        when(userRepository.findByEmail(loginDto.getEmail())).thenReturn(Optional.of(userEntity));

        when(authenticationManager.authenticate(any())).thenReturn(null);

        UserDto result = authenticationService.authenticate(loginDto);

        assertNotNull(result);
        assertEquals(userEntity.getId(), result.getId());
        assertEquals(userEntity.getEmail(), result.getEmail());
        assertEquals(userEntity.getName(), result.getName());
    }

    @Test
    void authenticate_UserNotFound() {
        when(userRepository.findByEmail(loginDto.getEmail())).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> authenticationService.authenticate(loginDto));
    }

    @Test
    void signUp_Success() {
        when(userRepository.findByEmail(registrationDto.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(registrationDto.getPassword())).thenReturn("encodedpassword");

        UserDto result = authenticationService.signUp(registrationDto);
        result.setId(1L);

        assertNotNull(result);
        assertEquals(registrationDto.getEmail(), result.getEmail());
        assertEquals(registrationDto.getName(), result.getName());
        assertEquals(registrationDto.getSurname(), result.getSurname());
        assertEquals(BigDecimal.valueOf(100), result.getBalance());
        verify(userRepository).saveAndFlush(any(UserEntity.class));
    }

    @Test
    void signUp_UserAlreadyExists() {
        when(userRepository.findByEmail(registrationDto.getEmail())).thenReturn(Optional.of(userEntity));

        assertThrows(UsernameException.class, () -> authenticationService.signUp(registrationDto));
    }
}
