package org.zxcjaba.casino.api.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zxcjaba.casino.api.dto.LoginDto;
import org.zxcjaba.casino.api.dto.LoginResponse;
import org.zxcjaba.casino.api.dto.RegistrationDto;
import org.zxcjaba.casino.api.dto.UserDto;
import org.zxcjaba.casino.api.service.AuthenticationService;
import org.zxcjaba.casino.api.service.JwtService;
import org.zxcjaba.casino.persistence.entity.UserEntity;

@RequestMapping("/auth")
@RestController
public class LoginController {

    private final JwtService jwtService;

    private final AuthenticationService authenticationService;

    public LoginController(JwtService jwtService,AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }
    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/signup")
    public ResponseEntity<UserDto> register(@RequestBody RegistrationDto registrationDto) {
        UserDto user=authenticationService.signUp(registrationDto);
        return ResponseEntity.ok(user);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginDto loginDto) {
        try {


            UserDto authenticatedUser = authenticationService.authenticate(loginDto);


            String token = jwtService.generateToken(UserEntity.builder()
                    .id(authenticatedUser.getId())
                    .name(authenticatedUser.getName())
                    .surname(authenticatedUser.getSurname())
                    .email(authenticatedUser.getEmail())
                    .password(authenticatedUser.getPassword())
                    .balance(authenticatedUser.getBalance())
                    .build()
            );


            LoginResponse response = new LoginResponse();
            response.setToken(token);
            response.setExpirationTime(jwtService.getExpirationTime());

            return ResponseEntity.ok(response);
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .build();
        }
    }

}
