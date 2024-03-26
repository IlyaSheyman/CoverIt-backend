package main_service.card.playlist.storage;

import main_service.card.playlist.entity.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaylistRepository extends JpaRepository<Playlist, Integer> {
    boolean existsByUrl(String url);
}
