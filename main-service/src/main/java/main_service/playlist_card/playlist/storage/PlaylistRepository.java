package main_service.playlist_card.playlist.storage;

import main_service.playlist_card.playlist.entity.Playlist;
import main_service.user.entity.User;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlaylistRepository extends JpaRepository<Playlist, Integer> {
    boolean existsByUrl(String url);
    List<Playlist> findByAuthor(User user, PageRequest pageRequest);
}
