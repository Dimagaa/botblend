package com.app.botblend.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.app.botblend.dto.chat.ChatDto;
import com.app.botblend.dto.chat.MessageDto;
import com.app.botblend.dto.chat.MessageSendRequestDto;
import com.app.botblend.exception.EntityNotFoundException;
import com.app.botblend.mapper.ChatMapper;
import com.app.botblend.mapper.MessageMapper;
import com.app.botblend.model.Chat;
import com.app.botblend.model.Message;
import com.app.botblend.repository.ChatRepository;
import com.app.botblend.repository.MessageRepository;
import com.app.botblend.service.MessageSenderService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class ChatServiceImplTest {
    private static final LocalDateTime CREATED_AT = LocalDateTime.of(
            2023, 10, 10, 10, 0, 0
    );
    private static final Pageable PAGEABLE = Pageable.ofSize(10);
    private static final Chat FIRST_CHAT = new Chat();
    private static final Chat SECOND_CHAT = new Chat();
    @Mock
    private MessageSenderService messageSenderService;
    @Mock
    private ChatRepository chatRepository;
    @Mock
    private MessageRepository messageRepository;
    @Mock
    private ChatMapper chatMapper;
    @Mock
    private MessageMapper messageMapper;

    @InjectMocks
    private ChatServiceImpl chatService;

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
    @DisplayName("Send Message: Chat found - Message sent successfully")
    void sendMessage_ChatFound_MessageSentSuccessfully() {
        Long chatId = 1L;
        MessageSendRequestDto requestDto = new MessageSendRequestDto(
                "admin", "First message: Chat 1"
        );
        Message message = new Message(1L,
                FIRST_CHAT,
                CREATED_AT,
                "admin",
                "First message: Chat 1",
                false);
        MessageDto expected = new MessageDto(
                message.getId(),
                message.getCreatedAt(),
                message.getSender(),
                message.getContent()
        );

        when(chatRepository.findById(chatId)).thenReturn(Optional.of(FIRST_CHAT));
        when(messageMapper.toModel(requestDto, FIRST_CHAT)).thenReturn(message);
        when(messageMapper.toDto(message)).thenReturn(expected);

        MessageDto actual = chatService.sendMessage(chatId, requestDto);

        assertEquals(expected, actual);
        verify(chatRepository, times(1)).findById(chatId);
        verify(messageSenderService, times(1)).sendMessage(message);
    }

    @Test
    @DisplayName("Send Message: Chat not found - Throw EntityNotFoundException")
    void sendMessage_ChatNotFound_ThrowEntityNotFoundException() {
        Long nonExistingChatId = 99L;
        MessageSendRequestDto requestDto = new MessageSendRequestDto(
                "admin", "First message: Chat 1"
        );

        when(chatRepository.findById(nonExistingChatId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> chatService.sendMessage(nonExistingChatId, requestDto),
                "Expected EntityNotFoundException when chat is not found");
    }

    @Test
    @DisplayName("Find All Chats: Chats found - Return List of ChatDtos")
    void findAll_ChatsFound_ReturnListOfChatDtos() {
        Page<Chat> chatList = new PageImpl<>(List.of(FIRST_CHAT, SECOND_CHAT));
        ChatDto chatDto = new ChatDto(1L, 1L);

        when(chatRepository.findAll(PAGEABLE)).thenReturn(chatList);
        when(chatMapper.toDto(any(Chat.class))).thenReturn(chatDto);

        List<ChatDto> actual = chatService.findAll(PAGEABLE);

        assertEquals(chatList.getSize(), actual.size(),
                "Expected list of %d, but was list of %d elements"
                        .formatted(chatList.getSize(), actual.size()));
        verify(chatRepository, times(1)).findAll(PAGEABLE);
    }

    @Test
    @DisplayName("Find All Chats: No chats found - Return empty List")
    void findAll_NoChatsFound_ReturnEmptyList() {
        Page<Chat> emptyChatPage = Page.empty();

        when(chatRepository.findAll(PAGEABLE)).thenReturn(emptyChatPage);

        List<ChatDto> result = chatService.findAll(PAGEABLE);

        assertTrue(result.isEmpty(), "Expected an empty list when no chats are found");
    }

    @Test
    @DisplayName("Get Messages By ChatId: Messages found - Return List of MessageDtos")
    void getMessagesByChatId_MessagesFound_ReturnListOfMessageDtos() {
        Long chatId = 1L;
        List<Message> messageList = FIRST_CHAT.getMessages();
        MessageDto expected = new MessageDto(
                1L,
                CREATED_AT,
                "user",
                "First message");

        when(messageRepository.findAllByChatId(chatId, PAGEABLE)).thenReturn(messageList);
        when(messageMapper.toDto(any(Message.class))).thenReturn(expected);

        List<MessageDto> actual = chatService.getMessagesByChatId(chatId, PAGEABLE);

        Assertions.assertEquals(messageList.size(), actual.size(),
                "Expected list of %d, but was list of %d elements"
                        .formatted(messageList.size(), actual.size()));
        verify(messageRepository, times(1)).findAllByChatId(chatId, PAGEABLE);
    }
}
