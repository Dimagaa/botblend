package com.app.botblend.repository;

import com.app.botblend.model.Chat;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    Optional<Chat> findByExternalId(Long externalId);
}
