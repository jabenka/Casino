package org.zxcjaba.casino.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameResultsDto {

    @JsonProperty("bet")
    private BigDecimal bet;

    @JsonProperty("color")
    private String color;

    @JsonProperty("rouletteNumber")
    private String rouletteNumber;

    // Геттеры и сеттеры

    public BigDecimal getBet() {
        return bet;
    }

    public void setBet(BigDecimal bet) {
        this.bet = bet;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getRouletteNumber() {
        return rouletteNumber;
    }

    public void setRouletteNumber(String rouletteNumber) {
        this.rouletteNumber = rouletteNumber;
    }
}
