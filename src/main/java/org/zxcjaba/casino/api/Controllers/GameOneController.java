package org.zxcjaba.casino.api.Controllers;

import jakarta.transaction.Transactional;
import org.apache.coyote.BadRequestException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.zxcjaba.casino.api.DTO.GameOneDto;
import org.zxcjaba.casino.api.DTO.GameResultsDto;
import org.zxcjaba.casino.api.Exceptions.BalanceException;
import org.zxcjaba.casino.persistence.Entity.UserEntity;
import org.zxcjaba.casino.persistence.Repository.UserRepository;
import java.math.BigDecimal;


@RestController
@RequestMapping("/roulette")
@Transactional
public class GameOneController {

    private final UserRepository userRepository;
    private final RabbitTemplate rabbitTemplate;

    public GameOneController(UserRepository userRepository, RabbitTemplate rabbitTemplate) {
        this.userRepository = userRepository;
        this.rabbitTemplate = rabbitTemplate;
    }


    @PostMapping("/play")
    public ResponseEntity<GameOneDto> play(@RequestParam(name="bet") BigDecimal bet,
                                           @RequestParam(name = "number",required = true) String number,
                                           @RequestParam(name="color")String color,
                                           @RequestParam(name="rouletteNumber")String rouletteNumber) throws BadRequestException {



        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();


        UserEntity entity = (UserEntity) authentication.getPrincipal();

        if(entity.getBalance().compareTo(bet) < 0) {
            return ResponseEntity.badRequest().build();
        }else{
            entity.setBalance(entity.getBalance().subtract(bet));
        }

        GameResultsDto results=GameResultsDto.builder()
                .bet(bet)
                .color(color)
                .rouletteNumber(rouletteNumber)
                .build();

        //check

        GameOneDto response;
        Short res=0;

        try{
            res=Short.valueOf(number);
        }catch(NumberFormatException e){
            return ResponseEntity.badRequest().build();
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
        rabbitTemplate.convertAndSend("game_result_queue",results);

        return ResponseEntity.ok(response);
    }

}
