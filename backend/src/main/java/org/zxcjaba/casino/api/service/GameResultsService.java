package org.zxcjaba.casino.api.service;


import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.zxcjaba.casino.api.dto.GameResultsDto;
import org.zxcjaba.casino.persistence.entity.GameOneResultsEntity;
import org.zxcjaba.casino.persistence.repository.GameOneResultsRepository;




//СТАТИСТИКАААА
@Service
public class GameResultsService {

    private final GameOneResultsRepository repository;

    public GameResultsService(GameOneResultsRepository repository) {
        this.repository = repository;
    }

    @RabbitListener(queues = "game_result_queue")
    public void receiveGameResult(GameResultsDto gameResultsDto) {
        try {
            if (gameResultsDto == null || gameResultsDto.getBet() == null || gameResultsDto.getColor() == null || gameResultsDto.getRouletteNumber() == null) {
                System.err.println("Получено пустое или неполное сообщение");
                return; // Просто не обрабатываем сообщение
            }

            // Обработка валидных данных
            GameOneResultsEntity entity = GameOneResultsEntity
                    .builder()
                    .bet(gameResultsDto.getBet())
                    .color(gameResultsDto.getColor())
                    .rouletteNumber(gameResultsDto.getRouletteNumber())
                    .build();

            repository.saveAndFlush(entity);
        } catch (Exception e) {
            // Логируем исключение и продолжаем
            System.err.println("Ошибка при обработке сообщения: " + e.getMessage());
        }
    }





}
