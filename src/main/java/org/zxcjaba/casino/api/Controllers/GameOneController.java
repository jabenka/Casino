package org.zxcjaba.casino.api.Controllers;


import jakarta.transaction.Transactional;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.zxcjaba.casino.Math.PseudorandomNumbers;
import org.zxcjaba.casino.api.DTO.GameOneDto;
import org.zxcjaba.casino.api.DTO.UserDto;
import org.zxcjaba.casino.api.Exceptions.BalanceException;
import org.zxcjaba.casino.api.Exceptions.RolutteException;
import org.zxcjaba.casino.persistence.Entity.UserEntity;
import org.zxcjaba.casino.persistence.Repository.UserRepository;

import java.math.BigDecimal;
import java.security.SecureRandom;

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



    @GetMapping("/play")
    public ResponseEntity<GameOneDto> play(@RequestParam(name="bet") BigDecimal bet,@RequestParam(name = "color",required = false) String color,
                                           @RequestParam(name = "number",required = false) Integer number) throws BadRequestException {



        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();


        UserEntity entity = (UserEntity) authentication.getPrincipal();

        if(entity.getBalance().compareTo(bet) < 0) {
            throw new BalanceException("Not enough money to play");
        }else{
            entity.setBalance(entity.getBalance().subtract(bet));
        }


        //check

        GameOneDto response=new GameOneDto();
        String win="";
        Long ind= 0L;
        Long Win= 0L;





        if(color!=null) {

           if(number!=null) {

               throw new RolutteException("you can only choose color or number");

           }else{

               SecureRandom random = new SecureRandom();
               int choice=random.nextInt();

               if(choice%2==0){

                   win="RED";

                   Win=2l;

               }else{

                   win="BLACK";

                   Win=2l;


               }
            }

        }else if(color==null&& number!=null) {

            ind=numbers.generateValueForRoulette();

            if(number.equals(ind)){

                Win=5l;

            }

        }else{
            Win=0L;
        }


        BigDecimal newBalance=entity.getBalance().add(bet.multiply(new BigDecimal(String.valueOf(Win))));

//        UserDto user=UserDto.builder()
//                .id(entity.getId())
//                .name(entity.getName())
//                .surname(entity.getSurname())
//                .email(entity.getEmail())
//                .password(entity.getPassword())
//                .balance(entity.getBalance())
//                .build();

        entity.setBalance(newBalance);

        userRepository.saveAndFlush(entity);

        response=GameOneDto.builder()
                .bet(bet)
                .newBalance(newBalance)
                .build();

        return ResponseEntity.ok(response);
    }

}
