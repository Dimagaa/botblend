package com.app.botblend.repository;

import com.app.botblend.model.Chat;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat, Long> {

    List<Chat> findAll();

    @EntityGraph(attributePaths = "messages")
    Optional<Chat> findById(Long id);

    Optional<Chat> findByExternalId(Long externalId);
}
