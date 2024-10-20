package org.zxcjaba.casino.api.DTO;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto {

    @NonNull
    @JsonProperty("id")
    Long id;

    @NonNull
    @JsonProperty("name")
    String name;

    @NonNull
    @JsonProperty("surname")
    String surname;

    @NonNull
    @JsonProperty("email")
    String email;

    @NonNull
    String password;

    @NonNull
    BigDecimal balance;



}
