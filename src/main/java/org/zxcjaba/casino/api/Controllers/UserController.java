package org.zxcjaba.casino.api.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.zxcjaba.casino.api.DTO.UserDto;
import org.zxcjaba.casino.persistence.Entity.UserEntity;

import java.math.BigDecimal;


@RestController
@RequestMapping("/user")
public class UserController {


    @CrossOrigin(origins = "http://localhost:3000",allowedHeaders ="*",allowCredentials = "true")
    @GetMapping("/me")
    public ResponseEntity<UserDto> authenticatedUser() {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();


        UserEntity entity = (UserEntity) authentication.getPrincipal();

        UserDto dto=UserDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .surname(entity.getSurname())
                .email(entity.getEmail())
                .password(entity.getPassword())
                .balance(entity.getBalance())
                .build();

        return ResponseEntity.ok(dto);

    }

    @GetMapping("/balance")
    public BigDecimal getBalance() {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        UserEntity entity = (UserEntity) authentication.getPrincipal();
        return entity.getBalance();
    }
}
