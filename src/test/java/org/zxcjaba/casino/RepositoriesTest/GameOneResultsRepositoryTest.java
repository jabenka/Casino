package org.zxcjaba.casino.RepositoriesTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.zxcjaba.casino.persistence.Entity.GameOneResultsEntity;
import org.zxcjaba.casino.persistence.Repository.GameOneResultsRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GameOneResultsRepositoryTest {

    @Mock
    private GameOneResultsRepository gameOneResultsRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveGameResult() {
        GameOneResultsEntity entity = new GameOneResultsEntity(null, new BigDecimal("100.00"), "Red", "17");

        when(gameOneResultsRepository.save(entity)).thenReturn(entity);

        GameOneResultsEntity savedEntity = gameOneResultsRepository.save(entity);

        assertNotNull(savedEntity);
        assertEquals(new BigDecimal("100.00"), savedEntity.getBet());
        assertEquals("Red", savedEntity.getColor());
        assertEquals("17", savedEntity.getRouletteNumber());

        verify(gameOneResultsRepository, times(1)).save(entity);
    }

    @Test
    public void testFindById() {
        GameOneResultsEntity entity = new GameOneResultsEntity(1L, new BigDecimal("200.00"), "Black", "32");

        when(gameOneResultsRepository.findById(1L)).thenReturn(Optional.of(entity));

        Optional<GameOneResultsEntity> foundEntity = gameOneResultsRepository.findById(1L);

        assertTrue(foundEntity.isPresent());
        assertEquals(1L, foundEntity.get().getId());
        assertEquals(new BigDecimal("200.00"), foundEntity.get().getBet());
        assertEquals("Black", foundEntity.get().getColor());
        assertEquals("32", foundEntity.get().getRouletteNumber());

        verify(gameOneResultsRepository, times(1)).findById(1L);
    }

    @Test
    public void testFindByIdNotFound() {
        when(gameOneResultsRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<GameOneResultsEntity> foundEntity = gameOneResultsRepository.findById(99L);
        assertFalse(foundEntity.isPresent());

        verify(gameOneResultsRepository, times(1)).findById(99L);
    }

    @Test
    public void testFindAll() {
        GameOneResultsEntity entity1 = new GameOneResultsEntity(1L, new BigDecimal("150.00"), "Red", "19");
        GameOneResultsEntity entity2 = new GameOneResultsEntity(2L, new BigDecimal("250.00"), "Black", "7");

        when(gameOneResultsRepository.findAll()).thenReturn(List.of(entity1, entity2));

        List<GameOneResultsEntity> allEntities = gameOneResultsRepository.findAll();

        assertNotNull(allEntities);
        assertEquals(2, allEntities.size());
        assertEquals("Red", allEntities.get(0).getColor());
        assertEquals("Black", allEntities.get(1).getColor());

        verify(gameOneResultsRepository, times(1)).findAll();
    }
}
