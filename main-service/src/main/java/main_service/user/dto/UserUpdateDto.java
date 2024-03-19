package main_service.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import static main_service.constants.Constants.*;

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
