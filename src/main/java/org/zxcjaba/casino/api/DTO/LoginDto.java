package org.zxcjaba.casino.api.DTO;
import lombok.*;
import lombok.experimental.FieldDefaults;


@Data
@Builder
@FieldDefaults(level=AccessLevel.PRIVATE)
public class LoginDto {

    String email;
    String password;


}