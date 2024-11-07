package org.zxcjaba.casino.api.Controllers;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zxcjaba.casino.api.DTO.LoginDto;
import org.zxcjaba.casino.api.DTO.LoginResponse;
import org.zxcjaba.casino.api.DTO.RegistrationDto;
import org.zxcjaba.casino.api.DTO.UserDto;
import org.zxcjaba.casino.api.Services.AuthenticationService;
import org.zxcjaba.casino.api.Services.JwtService;
import org.zxcjaba.casino.persistence.Entity.UserEntity;

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
        System.out.println("User received: " + registrationDto.toString());
        UserDto user=authenticationService.signUp(registrationDto);
        return ResponseEntity.ok(user);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginDto loginDto) {
        UserDto authenticatedUser=authenticationService.authenticate(loginDto);


        String token=jwtService.generateToken(UserEntity.builder()
                .id(authenticatedUser.getId())
                .name(authenticatedUser.getName())
                .surname(authenticatedUser.getSurname())
                .email(authenticatedUser.getEmail())
                .password(authenticatedUser.getPassword())
                .balance(authenticatedUser.getBalance())
                .build()
        );


        LoginResponse response=new LoginResponse();
        response.setToken(token);
        response.setExpirationTime(jwtService.getExpirationTime());

        return ResponseEntity.ok(response);
    }

}
