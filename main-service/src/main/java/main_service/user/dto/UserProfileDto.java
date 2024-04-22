package main_service.user.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserProfileDto {
    private int id;
    private String username;
    private String email;
    private int hiFiReleaseGenerations;
    private int loFiReleaseGenerations;
    private int hiFiPlaylistGenerations;
    private int loFiPlaylistGenerations;
}
