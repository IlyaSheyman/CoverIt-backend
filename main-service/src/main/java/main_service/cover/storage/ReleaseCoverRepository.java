package main_service.cover.storage;

import main_service.cover.entity.ReleaseCover;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReleaseCoverRepository extends JpaRepository<ReleaseCover, Integer> {
}
