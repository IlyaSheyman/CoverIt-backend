package main_service.user.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class PatronDto {
    private String email;
    private String fullName;
}
