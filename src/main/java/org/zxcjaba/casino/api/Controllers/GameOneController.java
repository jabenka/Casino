package org.zxcjaba.casino.api.Controllers;


import jakarta.transaction.Transactional;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.zxcjaba.casino.Math.PseudorandomNumbers;
import org.zxcjaba.casino.api.DTO.GameOneDto;
import org.zxcjaba.casino.api.DTO.UserDto;
import org.zxcjaba.casino.api.Exceptions.BalanceException;
import org.zxcjaba.casino.api.Exceptions.RolutteException;
import org.zxcjaba.casino.persistence.Entity.UserEntity;
import org.zxcjaba.casino.persistence.Repository.UserRepository;

import java.math.BigDecimal;
import java.security.SecureRandom;

import static java.lang.Math.abs;

@RestController
@RequestMapping("/roulette")
@Transactional
public class GameOneController {

    private final UserRepository userRepository;
    private final PseudorandomNumbers numbers;

    public GameOneController(UserRepository userRepository, PseudorandomNumbers numbers) {
        this.userRepository = userRepository;
        this.numbers = numbers;
    }



    @PostMapping("/play")
    public ResponseEntity<GameOneDto> play(@RequestParam(name="bet") BigDecimal bet,
                                           @RequestParam(name = "number",required = false) String number) throws BadRequestException {



        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();


        UserEntity entity = (UserEntity) authentication.getPrincipal();

        if(entity.getBalance().compareTo(bet) < 0) {
            throw new BalanceException("Not enough money to play");
        }else{
            entity.setBalance(entity.getBalance().subtract(bet));
        }

        System.out.println(number);

        //check

        GameOneDto response=new GameOneDto();
        String win="";
        Long ind= 0L;
        Long Win= 0L;

        Short res=0;

        try{
            res=Short.valueOf(number);
        }catch(NumberFormatException e){
            throw new BadRequestException("Frontend server error");
        }



        switch(res){
            case 0:
                break;
            case 1:
                entity.setBalance(bet.multiply(new BigDecimal("2")).add(entity.getBalance()));
            break;
            case 2:
                entity.setBalance(bet.multiply(new BigDecimal("10")).add(entity.getBalance()));
            break;
        }








        userRepository.saveAndFlush(entity);

        response=GameOneDto.builder()
                .bet(bet)
                .newBalance(entity.getBalance())
                .build();

        System.out.println("Response:"+response.toString());

        return ResponseEntity.ok(response);
    }

}
