package main_service.exception.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class LimitExceptionMessage {
    private String header;
    private int hiFiLeft;
    private int loFiLeft;
    private Integer hoursLeft;
    private Integer minutesLeft;
    private String status;
    private String timestamp;
}
