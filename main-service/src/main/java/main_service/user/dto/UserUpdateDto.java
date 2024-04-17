package main_service.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import static main_service.constants.Constants.MAX_USERNAME_LENGTH;
import static main_service.constants.Constants.MIN_USERNAME_LENGTH;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserUpdateDto {

    @Size(
            min = MIN_USERNAME_LENGTH,
            max = MAX_USERNAME_LENGTH
    )
    private @NotNull @NotBlank String username;
}
