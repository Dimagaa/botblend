package com.app.botblend.contoller;

import com.app.botblend.dto.user.UserDto;
import com.app.botblend.dto.user.UserRegisterRequestDto;
import com.app.botblend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Users management API", description = "Register and authenticate users")
@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Operation(
            summary = "Retrieve Current User Information",
            description = "Get Information of the Currently Authenticated User"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public UserDto getCurrent() {
        return userService.getCurrent();
    }

    @Operation(
            summary = "Register a new user",
            description = "Register a new user with the provided information"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/register")
    public UserDto register(@RequestBody @Valid UserRegisterRequestDto requestDto) {
        return userService.register(requestDto);
    }

    @Operation(
            summary = "Update User Information",
            description = "Update user with the provided information"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto update(@RequestBody @Valid UserRegisterRequestDto requestDto) {
        return userService.updateCurrentUser(requestDto);
    }

    @Operation(
            summary = "Delete a user",
            description = "Delete a user with the provided id"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        userService.delete(id);
    }
}
