package main_service.admin.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatisticsDto {
    @JsonProperty("playlist_saved_number")
    public Integer playlistsSaved;
    @JsonProperty("release_saved_number")
    public Integer releasesSaved;
    @JsonProperty("active_users_number")
    public Integer activeUsers;
    @JsonProperty("users_total_number")
    public Integer usersTotal;
    @JsonProperty("generations_number")
    public Integer generationsNumber;
}
