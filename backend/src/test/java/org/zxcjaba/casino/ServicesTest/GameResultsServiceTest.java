package org.zxcjaba.casino.ServicesTest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.zxcjaba.casino.api.dto.GameResultsDto;
import org.zxcjaba.casino.api.service.GameResultsService;
import org.zxcjaba.casino.persistence.entity.GameOneResultsEntity;
import org.zxcjaba.casino.persistence.repository.GameOneResultsRepository;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GameResultsServiceTest {

    @Mock
    private GameOneResultsRepository repository;

    @InjectMocks
    private GameResultsService gameResultsService;



    @Test
    void testReceiveGameResult_validData() {
        GameResultsDto dto = new GameResultsDto();
        dto.setBet(new BigDecimal("100"));
        dto.setColor("Red");
        dto.setRouletteNumber("12");

        gameResultsService.receiveGameResult(dto);

        verify(repository, times(1)).saveAndFlush(Mockito.any(GameOneResultsEntity.class));
    }

    @Test
    void testReceiveGameResult_nullMessage() {
        gameResultsService.receiveGameResult(null);

        verify(repository, times(0)).saveAndFlush(Mockito.any(GameOneResultsEntity.class));
    }

    @Test
    void testReceiveGameResult_incompleteMessage() {
        GameResultsDto dto = new GameResultsDto();
        dto.setBet(null);
        dto.setColor("Red");
        dto.setRouletteNumber("12");

        gameResultsService.receiveGameResult(dto);

        verify(repository, times(0)).saveAndFlush(Mockito.any(GameOneResultsEntity.class));
    }

    @Test
    void testReceiveGameResult_invalidData() {
        GameResultsDto dto = new GameResultsDto();
        dto.setBet(new BigDecimal("100"));
        dto.setColor("Red");
        dto.setRouletteNumber(null);

        gameResultsService.receiveGameResult(dto);

        verify(repository, times(0)).saveAndFlush(Mockito.any(GameOneResultsEntity.class));
    }

    @Test
    void testReceiveGameResult_exceptionHandling() {
        GameResultsDto dto = new GameResultsDto();
        dto.setBet(new BigDecimal("100"));
        dto.setColor("Red");
        dto.setRouletteNumber("12");

        doThrow(new RuntimeException("Database error")).when(repository).saveAndFlush(Mockito.any(GameOneResultsEntity.class));

        gameResultsService.receiveGameResult(dto);

        verify(repository, times(1)).saveAndFlush(Mockito.any(GameOneResultsEntity.class));
    }
}
