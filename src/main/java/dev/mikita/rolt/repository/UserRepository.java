package dev.mikita.rolt.repository;

import dev.mikita.rolt.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query(name = "User.findByEmail")
    Optional<User> findByEmail(String email);
}
