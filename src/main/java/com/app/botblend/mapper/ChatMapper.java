package com.app.botblend.mapper;

import com.app.botblend.config.MapperConfig;
import com.app.botblend.dto.chat.ChatDto;
import com.app.botblend.model.Chat;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class, uses = MessageMapper.class)
public interface ChatMapper {
    ChatDto toDto(Chat model);
}
