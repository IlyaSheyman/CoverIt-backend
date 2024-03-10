package main_service.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import static main_service.constants.Constants.*;
import static main_service.constants.Constants.MAX_USER_EMAIL_LENGTH;

@Data
@Schema(description = "Запрос на аутентификацию")
public class SignInRequest {


    @Schema(description = "Адрес электронной почты", example = "jondoe@gmail.com")
    @Size(min = MIN_USER_EMAIL_LENGTH, max = MAX_USER_EMAIL_LENGTH)
    @NotBlank(message = "Адрес электронной почты не может быть пустыми")
    @Email(message = "Email адрес должен быть в формате user@example.com")
    private String email;

    @Schema(description = "Пароль", example = "my_1secret1_password")
    @Size(min = MIN_USER_PASSWORD_LENGTH, max = MAX_USER_PASSWORD_LENGTH)
    private String password;

}