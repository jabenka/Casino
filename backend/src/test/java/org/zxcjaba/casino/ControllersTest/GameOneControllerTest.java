package org.zxcjaba.casino.ControllersTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.zxcjaba.casino.api.controller.GameOneController;
import org.zxcjaba.casino.api.dto.GameResultsDto;
import org.zxcjaba.casino.persistence.entity.UserEntity;
import org.zxcjaba.casino.persistence.repository.UserRepository;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(MockitoExtension.class)
public class GameOneControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private GameOneController gameOneController;

    private MockMvc mockMvc;

    private UserEntity mockUser;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(gameOneController).build();

        mockUser = new UserEntity();
        mockUser.setId(1L);
        mockUser.setBalance(new BigDecimal("1000.00"));

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(mockUser);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    void testPlay_withValidBet() throws Exception {
        BigDecimal bet = new BigDecimal("100.0");
        String number = "1";
        String color = "red";
        String rouletteNumber = "10";

        when(userRepository.saveAndFlush(any(UserEntity.class))).thenReturn(mockUser);

        mockMvc.perform(post("/roulette/play")
                        .param("bet", bet.toString())
                        .param("number", number)
                        .param("color", color)
                        .param("rouletteNumber", rouletteNumber)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bet").value("100.0"))
                .andExpect(jsonPath("$.newBalance").value("1100.0"));

        verify(userRepository, times(1)).saveAndFlush(any(UserEntity.class));
        verify(rabbitTemplate, times(1)).convertAndSend(eq("game_result_queue"), any(GameResultsDto.class));
    }

    @Test
    void testPlay_withInsufficientBalance() throws Exception {
        BigDecimal bet = new BigDecimal("2000.0");
        String number = "1";
        String color = "red";
        String rouletteNumber = "10";

        mockMvc.perform(post("/roulette/play")
                        .param("bet", bet.toString())
                        .param("number", number)
                        .param("color", color)
                        .param("rouletteNumber", rouletteNumber)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(userRepository, times(0)).saveAndFlush(any(UserEntity.class));
    }

    @Test
    void testPlay_withInvalidNumber() throws Exception {
        BigDecimal bet = new BigDecimal("100.00");
        String number = "invalid";
        String color = "red";
        String rouletteNumber = "10";

        mockMvc.perform(post("/roulette/play")
                        .param("bet", bet.toString())
                        .param("number", number)
                        .param("color", color)
                        .param("rouletteNumber", rouletteNumber)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(userRepository, times(0)).saveAndFlush(any(UserEntity.class));
    }

    @Test
    void testPlay_withColorAndRouletteNumber() throws Exception {
        BigDecimal bet = new BigDecimal("50.00");
        String number = "2";
        String color = "black";
        String rouletteNumber = "10";
        String newBalance=mockUser.getBalance().add(bet.multiply(new BigDecimal(10)).subtract(bet)).toString();
        newBalance=newBalance.substring(0,newBalance.length()-1);
        when(userRepository.saveAndFlush(any(UserEntity.class))).thenReturn(mockUser);

        mockMvc.perform(post("/roulette/play")
                        .param("bet", bet.toString())
                        .param("number", number)
                        .param("color", color)
                        .param("rouletteNumber", rouletteNumber)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bet").value("50.0"))
                .andExpect(jsonPath("$.newBalance").value(newBalance));

        verify(userRepository, times(1)).saveAndFlush(any(UserEntity.class));
        verify(rabbitTemplate, times(1)).convertAndSend(eq("game_result_queue"), any(GameResultsDto.class));
    }
}
