package com.app.botblend.mapper;

import com.app.botblend.config.MapperConfig;
import com.app.botblend.dto.chat.MessageDto;
import com.app.botblend.dto.chat.MessageSendRequestDto;
import com.app.botblend.model.Chat;
import com.app.botblend.model.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface MessageMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    Message toModel(MessageSendRequestDto dto, Chat chat);

    MessageDto toDto(Message model);
}
