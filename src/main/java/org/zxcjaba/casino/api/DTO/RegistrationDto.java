package org.zxcjaba.casino.api.DTO;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegistrationDto {

    String name;

    String surname;

    String email;

    String password;


}
