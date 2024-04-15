package main_service.user.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserSmallDto {
    private int id;
    private String username;
    private String email;
    private int playlistGenerations;
}
