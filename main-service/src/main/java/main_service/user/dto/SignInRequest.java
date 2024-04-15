package main_service.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import static main_service.constants.Constants.*;

@Data
@Schema(description = "Sign in request")
public class SignInRequest {


    @Schema(description = "Email address", example = "jondoe@gmail.com")
    @Size(min = MIN_USER_EMAIL_LENGTH, max = MAX_USER_EMAIL_LENGTH)
    @NotBlank(message = "Email address can not be blank")
    @Email(message = "Email address should be formatted as user@example.com")
    private String email;

    @Schema(description = "Password", example = "kozyasolov12!")
    @Size(min = MIN_USER_PASSWORD_LENGTH, max = MAX_USER_PASSWORD_LENGTH)
    private String password;

}