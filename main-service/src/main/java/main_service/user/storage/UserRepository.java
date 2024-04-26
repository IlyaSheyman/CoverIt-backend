package main_service.user.storage;

import main_service.user.entity.User;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
    @Query("SELECT u FROM User u WHERE u.verificationCode = ?1")
    User findByVerificationCode(String code);
    User getByUsername(String username);

    User getByEmail(String email);
    boolean existsByUsernameIgnoreCaseAndEnabled(String username, boolean enabled);
    boolean existsByEmailIgnoreCaseAndEnabled(String email, boolean enabled);
    List<User> findByUsernameContaining(String search, PageRequest pageRequest);
    List<User> findBySubscribedTrue();
    User findByPatronName(String patronName);
}