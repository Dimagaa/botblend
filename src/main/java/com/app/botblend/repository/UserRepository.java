package com.app.botblend.repository;

import com.app.botblend.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {
    @EntityGraph(attributePaths = "roles")
    Optional<User> findByEmail(String email);

    @Query("""
            FROM User u
            WHERE u.id = ?#{ principal?.id }
            """)
    User getCurrentUser();
}
