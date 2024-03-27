package image_client.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrackDto {
    private List<String> authors;
    private String title;

    @Override
    public String toString() {
        return "Track{" +
                "authors=" + authors +
                ", title='" + title + '\'' +
                '}';
    }
}