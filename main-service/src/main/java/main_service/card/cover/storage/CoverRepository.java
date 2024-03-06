package main_service.card.cover.storage;

import main_service.card.cover.entity.Cover;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoverRepository extends JpaRepository<Cover, Integer> {
}
