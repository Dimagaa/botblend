package com.app.botblend.repository;

import com.app.botblend.model.Chat;
import com.app.botblend.model.Message;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MessageRepositoryTest {
    private static final String INSERT_DATA_SCRIPT =
            "classpath:sqlscripts/insert-chats&messages.sql";
    private static final String DELETE_DATA_SCRIPT =
            "classpath:sqlscripts/delete-chats&messages.sql";
    private static final LocalDateTime CREATED_AT = LocalDateTime.of(
            2023, 10, 10, 10, 0, 0
    );
    private static final Pageable PAGEABLE = Pageable.ofSize(10);
    private static final Chat FIRST_CHAT = new Chat();
    private static final Chat SECOND_CHAT = new Chat();

    @Autowired
    private MessageRepository messageRepository;

    @BeforeAll
    static void beforeAll() {
        final Message message1 = new Message(1L,
                FIRST_CHAT,
                CREATED_AT,
                "admin",
                "First message: Chat 1",
                false);

        final Message message2 = new Message(2L,
                FIRST_CHAT,
                CREATED_AT.plusHours(1),
                "user",
                "Second message: Chat 1",
                false);

        final Message message3 = new Message(3L,
                FIRST_CHAT,
                CREATED_AT.plusHours(2),
                "assistant",
                "Third message: Chat 1",
                false);

        final Message message4 = new Message(4L,
                SECOND_CHAT,
                CREATED_AT.plusHours(3),
                "admin",
                "Forth message: Chat 2",
                false);

        final Message message5 = new Message(5L,
                SECOND_CHAT,
                CREATED_AT.plusHours(4),
                "user",
                "Fifth message: Chat 2",
                false);

        final Message message6 = new Message(6L,
                SECOND_CHAT,
                CREATED_AT.plusHours(5),
                "system",
                "System deleted message",
                true);

        FIRST_CHAT.setId(1L);
        FIRST_CHAT.setExternalId(1L);
        FIRST_CHAT.setMessages(List.of(message1, message2, message3));

        SECOND_CHAT.setId(2L);
        SECOND_CHAT.setExternalId(2L);
        SECOND_CHAT.setMessages(List.of(message4, message5, message6));
    }

    @Test
    @DisplayName("Find all by ChatId when the chat exists - Ok")
    @Sql(scripts = INSERT_DATA_SCRIPT,
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = DELETE_DATA_SCRIPT,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findAllByChatId_WhenChatExist_ReturnListOfMessages() {
        List<Message> expectedForFirst = FIRST_CHAT.getMessages();
        List<Message> expectedForSecond = SECOND_CHAT.getMessages()
                .stream()
                .filter(message -> !message.isDeleted())
                .toList();

        List<Message> actualForFirst = messageRepository.findAllByChatId(
                FIRST_CHAT.getId(), PAGEABLE
        );
        List<Message> actualForSecond = messageRepository.findAllByChatId(
                SECOND_CHAT.getId(), PAGEABLE
        );

        Assertions.assertIterableEquals(expectedForFirst, actualForFirst);
        Assertions.assertIterableEquals(expectedForSecond, actualForSecond);
    }

    @Test
    @DisplayName("Find all by ChatId when the chat does not exist - EmptyList")
    @Sql(scripts = INSERT_DATA_SCRIPT,
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = DELETE_DATA_SCRIPT,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findAllByChatId_WhenChatNotExist_ReturnEmptyList() {
        List<Message> actual = messageRepository.findAllByChatId(
                3L, PAGEABLE
        );

        Assertions.assertNotNull(actual);
        Assertions.assertTrue(actual.isEmpty());
    }
}
