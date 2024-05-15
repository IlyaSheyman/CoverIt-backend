package main_service.cover.storage;

import main_service.cover.entity.Cover;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface CoverRepository extends JpaRepository<Cover, Integer> {
    Cover findByLink(String link);
    List<Cover> findAllByIsSavedFalseAndCreatedBefore(LocalDateTime expiration);
}
