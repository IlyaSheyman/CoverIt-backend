package main_service.user.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserLogsDto {
    private int id;
    private String username;
    private String email;
}