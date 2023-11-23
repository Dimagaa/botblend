package com.app.botblend.repository;

import com.app.botblend.model.Chat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ChatRepositoryTest {

    private static final String INSERT_DATA_SCRIPT =
            "classpath:sqlscripts/insert-chats&messages.sql";
    private static final String DELETE_DATA_SCRIPT =
            "classpath:sqlscripts/delete-chats&messages.sql";
    private static final LocalDateTime CREATED_AT = LocalDateTime.of(
            2023, 10, 10, 10, 0, 0
    );
    private static final Chat FIRST_CHAT = new Chat();
    private static final Chat SECOND_CHAT = new Chat();

    @Autowired
    private ChatRepository chatRepository;

    @BeforeAll
    static void beforeAll() {
        FIRST_CHAT.setId(1L);
        FIRST_CHAT.setExternalId(1L);
        FIRST_CHAT.setMessages(List.of());

        SECOND_CHAT.setId(2L);
        SECOND_CHAT.setExternalId(2L);
        SECOND_CHAT.setMessages(List.of());
    }

    @Test
    @DisplayName("Find Chat by external id: When chat exists - Return Optional<Chat>")
    @Sql(scripts = INSERT_DATA_SCRIPT,
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = DELETE_DATA_SCRIPT,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findByExternalId_WhenChatExist_ReturnOptionalOfChat() {
        String message = "Expected chat to be present with external id: %d";

        Optional<Chat> actualFirst = chatRepository.findByExternalId(FIRST_CHAT.getExternalId());
        Optional<Chat> actualSecond = chatRepository.findByExternalId(SECOND_CHAT.getExternalId());

        Assertions.assertTrue(
                actualFirst.isPresent(),
                message.formatted(FIRST_CHAT.getExternalId())
        );
        Assertions.assertTrue(
                actualSecond.isPresent(),
                message.formatted(FIRST_CHAT.getExternalId()));
        Assertions.assertEquals(FIRST_CHAT, actualFirst.get());
        Assertions.assertEquals(SECOND_CHAT, actualSecond.get());
    }

    @Test
    @DisplayName("Find Chat by external id: When chat doesn't exist - Return Empty Optional")
    @Sql(scripts = INSERT_DATA_SCRIPT,
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = DELETE_DATA_SCRIPT,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findByExternalId_WhenChatNotExist_ReturnEmptyOptional() {
        Optional<Chat> actual = chatRepository.findById(3L);

        Assertions.assertTrue(
                actual.isEmpty(),
                "Expected an empty Optional for a non-existing chat"
        );
    }
}
