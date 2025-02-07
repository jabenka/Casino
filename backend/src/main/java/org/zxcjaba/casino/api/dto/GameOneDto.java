package org.zxcjaba.casino.api.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class GameOneDto {

    BigDecimal bet;

    BigDecimal newBalance;
}
