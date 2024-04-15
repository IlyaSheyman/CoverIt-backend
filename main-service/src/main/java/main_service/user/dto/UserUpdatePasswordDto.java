package main_service.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import main_service.config.passay.ValidPassword;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserUpdatePasswordDto {

    @ValidPassword
    private @NotNull @NotBlank String password;

    @ValidPassword
    private @NotNull @NotBlank String confirmPassword;
}
