package com.app.botblend.contoller;

import com.app.botblend.dto.UserDto;
import com.app.botblend.dto.UserRegisterRequestDto;
import com.app.botblend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authentication API", description = "Register and authenticate users")
@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class UserController {
    private final UserService userService;

    @Operation(
            summary = "Register a new user",
            description = "Register a new user with the provided information"
    )
    @PostMapping("/register")
    UserDto register(@RequestBody @Valid UserRegisterRequestDto requestDto) {
        return userService.register(requestDto);
    }
}
