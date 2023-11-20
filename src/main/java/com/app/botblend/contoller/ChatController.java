package com.app.botblend.contoller;

import com.app.botblend.dto.chat.ChatDto;
import com.app.botblend.dto.chat.MessageSendRequestDto;
import com.app.botblend.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "Chats Management API",
        description = "Provides endpoints for managing chats and sending messages"
)
@RequiredArgsConstructor
@RestController
@RequestMapping("/chats")
public class ChatController {
    private final ChatService chatService;

    @Operation(
            summary = "Get All chats",
            description = "Retrieve a list of all chats"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<ChatDto> getAll() {
        return chatService.findAll();
    }

    @Operation(
            summary = "Get chat by ID",
            description = "Retrieve a specific chat by its unique identifier"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ChatDto getById(@PathVariable Long id) {
        return chatService.getById(id);
    }

    @Operation(
            summary = "Send message by chat ID",
            description = "Send a message to a client using the identifier of the chat"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{chatId}")
    public ChatDto sendMessage(
            @PathVariable Long chatId,
            @RequestBody @Valid MessageSendRequestDto message
    ) {
        return chatService.sendMessage(chatId, message);
    }
}
