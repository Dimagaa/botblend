package com.app.botblend.contoller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.app.botblend.config.TelegramTestConfig;
import com.app.botblend.dto.chat.ChatDto;
import com.app.botblend.dto.chat.MessageDto;
import com.app.botblend.dto.chat.MessageSendRequestDto;
import com.app.botblend.model.Chat;
import com.app.botblend.model.Message;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@Import(TelegramTestConfig.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ChatControllerTest {
    protected static MockMvc mockMvc;
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
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();

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

    @SneakyThrows
    @Test
    @DisplayName("Get All Chats: Admin user - Return List of ChatDtos")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Sql(scripts = INSERT_DATA_SCRIPT,
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = DELETE_DATA_SCRIPT,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getAll_AdminUser_ReturnListOfChatDtos() {
        List<ChatDto> expected = Stream.of(FIRST_CHAT, SECOND_CHAT)
                .map(this::toChatDto).toList();

        MockHttpServletResponse response = mockMvc.perform(get("/chats")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        List<ChatDto> actual = objectMapper.readValue(
                response.getContentAsByteArray(),
                new TypeReference<>() {
                }
        );

        Assertions.assertIterableEquals(expected, actual);
    }

    @SneakyThrows
    @Test
    @DisplayName("Get All Chats: No Chats Available - Return Empty List")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getAll_NoChatsAvailable_ReturnEmptyList() {
        MockHttpServletResponse response = mockMvc.perform(get("/chats")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        List<ChatDto> actual = objectMapper.readValue(
                response.getContentAsByteArray(),
                new TypeReference<>() {
                }
        );

        Assertions.assertTrue(actual.isEmpty(), "Expected Empty List when chat doesn't exist");
    }

    @SneakyThrows
    @Test
    @DisplayName("Get All Non Authenticated User Attempt - Return 401")
    @Sql(scripts = INSERT_DATA_SCRIPT,
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = DELETE_DATA_SCRIPT,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getAll_NonAuthenticatedAttempt_Return401() {
        mockMvc.perform(get("/chats")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @SneakyThrows
    @TestFactory
    @DisplayName("Get Chat By ID: Admin user - Return List of MessageDtos")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Sql(scripts = INSERT_DATA_SCRIPT,
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = DELETE_DATA_SCRIPT,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    Stream<DynamicTest> getById_AdminUser_ReturnListOfMessageDtos() {
        return Stream.of(FIRST_CHAT, SECOND_CHAT)
                .map(chat -> DynamicTest.dynamicTest(
                        "Get chat by id: " + chat.getId(),
                        () -> {
                            List<MessageDto> expected = chat.getMessages()
                                    .stream()
                                    .filter(message -> !message.isDeleted())
                                    .map(this::toMessageDto)
                                    .sorted(Comparator.comparingLong(MessageDto::id))
                                    .toList();

                            MockHttpServletResponse responseForFirstChat = mockMvc.perform(
                                            get("/chats/" + chat.getId())
                                                    .contentType(MediaType.APPLICATION_JSON))
                                    .andExpect(status().isOk())
                                    .andReturn()
                                    .getResponse();

                            List<MessageDto> actual = objectMapper.readValue(
                                    responseForFirstChat.getContentAsByteArray(),
                                    new TypeReference<>() {
                                    });

                            actual.sort(Comparator.comparingLong(MessageDto::id));

                            Assertions.assertIterableEquals(expected, actual);
                        }
                ));
    }

    @SneakyThrows
    @Test
    @DisplayName("Get Chat By ID: Chat ID Not Found - Return Empty List")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Sql(scripts = INSERT_DATA_SCRIPT,
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = DELETE_DATA_SCRIPT,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getById_ChatIdNotFound_ReturnEmptyList() {
        long nonExistingId = -99L;
        MockHttpServletResponse response = mockMvc.perform(
                        get("/chats/" + nonExistingId)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        List<MessageDto> actual = objectMapper.readValue(
                response.getContentAsByteArray(),
                new TypeReference<>() {
                }
        );

        Assertions.assertTrue(actual.isEmpty(), "Expected Empty List");
    }

    @SneakyThrows
    @Test
    @DisplayName("Get Chat By ID: Non Authenticated User Attempt - Return 401")
    @Sql(scripts = INSERT_DATA_SCRIPT,
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = DELETE_DATA_SCRIPT,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getById_NonAuthenticatedAttempt_Return401() {
        mockMvc.perform(get("/chats/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @SneakyThrows
    @Test
    @DisplayName("Send Message: Admin user - Return MessageDto")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Sql(scripts = INSERT_DATA_SCRIPT,
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = DELETE_DATA_SCRIPT,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void sendMessage_AdminUser_ReturnMessageDto() {
        MessageSendRequestDto requestDto = new MessageSendRequestDto(
                "test-user", "Hello, User!");
        String request = objectMapper.writeValueAsString(requestDto);

        MockHttpServletResponse response = mockMvc.perform(
                        post("/chats/2")
                                .content(request)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse();

        MessageDto actual = objectMapper.readValue(
                response.getContentAsByteArray(),
                new TypeReference<>() {
                });

        Assertions.assertEquals(requestDto.sender(), actual.sender());
        Assertions.assertEquals(requestDto.content(), actual.content());
    }

    @TestFactory
    @DisplayName("Send Message: Invalid Input - Return 400")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Sql(scripts = INSERT_DATA_SCRIPT,
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = DELETE_DATA_SCRIPT,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    Stream<DynamicTest> sendMessage_InvalidInput_Return400() {
        return getInvalidMessageRequestDto()
                .map(message -> DynamicTest.dynamicTest(
                        "Try send message: " + message.toString(),
                        () -> {
                            String request = objectMapper.writeValueAsString(message);

                            mockMvc.perform(post("/chats/1")
                                            .content(request)
                                            .contentType(MediaType.APPLICATION_JSON)
                                    )
                                    .andExpect(status().isBadRequest());
                        })
                );
    }

    @SneakyThrows
    @Test
    @DisplayName("Send Message: Chat ID Not Found - 404")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void sendMessage_ChatIdNotFound_Return404() {
        long notExistingId = -99L;
        MessageSendRequestDto requestDto = new MessageSendRequestDto(
                "test-user", "Hello, User!");
        String request = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(post("/chats/" + notExistingId)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    private ChatDto toChatDto(Chat chat) {
        return new ChatDto(chat.getId(), chat.getExternalId());
    }

    private MessageDto toMessageDto(Message message) {
        return new MessageDto(
                message.getId(),
                message.getCreatedAt(),
                message.getSender(),
                message.getContent()
        );
    }

    private Stream<MessageSendRequestDto> getInvalidMessageRequestDto() {
        MessageSendRequestDto invalidRequestDto1 = new MessageSendRequestDto(
                "", "Hello, User!");
        MessageSendRequestDto invalidRequestDto2 = new MessageSendRequestDto(
                "user", "");
        MessageSendRequestDto invalidRequestDto3 = new MessageSendRequestDto(
                null, null);
        return Stream.of(invalidRequestDto1, invalidRequestDto2, invalidRequestDto3);
    }
}
