package com.app.botblend.mapper;

import com.app.botblend.config.MapperConfig;
import com.app.botblend.dto.UserDto;
import com.app.botblend.dto.UserRegisterRequestDto;
import com.app.botblend.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    User toModel(UserRegisterRequestDto requestDto);

    UserDto toDto(User user);
}
