package com.app.botblend.service;

import com.app.botblend.dto.UserDto;
import com.app.botblend.dto.UserRegisterRequestDto;

public interface UserService {
    UserDto register(UserRegisterRequestDto userRegisterRequest);
}
