package org.zxcjaba.casino.api.Services;


import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.zxcjaba.casino.api.DTO.LoginDto;
import org.zxcjaba.casino.api.DTO.RegistrationDto;
import org.zxcjaba.casino.api.DTO.UserDto;
import org.zxcjaba.casino.persistence.Entity.UserEntity;
import org.zxcjaba.casino.persistence.Repository.UserRepository;

import java.math.BigDecimal;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    public UserDto authenticate(LoginDto loginDto) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getEmail(),
                        loginDto.getPassword()
                )
        );
        UserEntity entity=userRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(()-> new UsernameNotFoundException("User not found"));
        return UserDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .surname(entity.getSurname())
                .email(entity.getEmail())
                .password(entity.getPassword())
                .balance(entity.getBalance())
                .build();

    }


    public UserDto signUp(RegistrationDto registrationDto) {
            UserEntity user=new UserEntity();

            user.setEmail(registrationDto.getEmail());
            user.setName(registrationDto.getName());
            user.setSurname(registrationDto.getSurname());
            user.setBalance(BigDecimal.valueOf(100));
            user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));

            userRepository.saveAndFlush(user);

            return UserDto.builder()
                    .id(user.getId())
                    .name(user.getName())
                    .surname(user.getSurname())
                    .email(user.getEmail())
                    .password(user.getPassword())
                    .balance(new BigDecimal(String.valueOf(100)))
                    .build();

    }




}
