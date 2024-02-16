package main_service.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import main_service.config.passay.ValidPassword;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
