package main_service.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import main_service.config.passay.ValidPassword;

import static main_service.constants.Constants.MAX_USER_EMAIL_LENGTH;
import static main_service.constants.Constants.MIN_USER_EMAIL_LENGTH;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserLoginDto {

    @Size(
            min = MIN_USER_EMAIL_LENGTH,
            max = MAX_USER_EMAIL_LENGTH
    )
    private @NotNull @Email String email;

    @ValidPassword
    private @NotNull @NotBlank String password;

}
