package main_service.user.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserSubscriptionDto {
    private String email;
    private boolean subscribed;
    private int generationsLeft;
    private LocalDateTime generationsUpdateAt;
}
