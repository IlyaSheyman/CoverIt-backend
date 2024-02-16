package main_service.user.storage;

import main_service.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmail(String email);

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByNameIgnoreCase(String username);
}
