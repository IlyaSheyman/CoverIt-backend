package main_service.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import main_service.config.passay.ValidPassword;

import static main_service.constants.Constants.*;

@Data
@Schema(description = "Sign up request")
public class SignUpRequest {
    private static final String USERNAME_PATTERN = "^(?=\\S+$).*$";

    @Schema(description = "Username", example = "Jon")
    @Size(min = MIN_USERNAME_LENGTH, max = MAX_USERNAME_LENGTH)
    @NotBlank(message = "Username shouldn't be blank")
    @Pattern(regexp = USERNAME_PATTERN, message = "Username should not contain spaces")
    private String username;

    @Schema(description = "User's email address", example = "jondoe@gmail.com")
    @Size(min = MIN_USER_EMAIL_LENGTH, max = MAX_USER_EMAIL_LENGTH)
    @NotBlank(message = "User's email address shouldn't blank")
    @Email(message = "Email address should be formatted as user@example.com")
    private String email;

    @Schema(description = "Пароль", example = "kozyasolov12!")
    @Size(min = MIN_USER_PASSWORD_LENGTH, max = MAX_USER_PASSWORD_LENGTH)
    @ValidPassword
    private String password;
}