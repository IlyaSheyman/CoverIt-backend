package main_service.cover.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CoverSmallDto {
    private String link;
    private Boolean isAbstract;
    private Boolean isLoFi;
}