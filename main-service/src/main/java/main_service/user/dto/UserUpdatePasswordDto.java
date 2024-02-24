package main_service.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
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
