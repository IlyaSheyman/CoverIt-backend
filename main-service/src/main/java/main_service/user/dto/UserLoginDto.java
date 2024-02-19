package main_service.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import main_service.config.passay.ValidPassword;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import static main_service.constants.Constants.*;
import static main_service.constants.Constants.MAX_USER_EMAIL_LENGTH;

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
