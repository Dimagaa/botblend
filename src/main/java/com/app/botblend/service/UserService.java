package com.app.botblend.service;

import com.app.botblend.dto.user.UserDto;
import com.app.botblend.dto.user.UserRegisterRequestDto;

public interface UserService {
    UserDto register(UserRegisterRequestDto userRegisterRequest);
}
