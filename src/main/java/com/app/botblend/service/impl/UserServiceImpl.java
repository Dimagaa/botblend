package com.app.botblend.service.impl;

import com.app.botblend.dto.UserDto;
import com.app.botblend.dto.UserRegisterRequestDto;
import com.app.botblend.exception.RegistrationException;
import com.app.botblend.mapper.UserMapper;
import com.app.botblend.model.Role;
import com.app.botblend.model.User;
import com.app.botblend.repository.RoleRepository;
import com.app.botblend.repository.UserRepository;
import com.app.botblend.service.UserService;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDto register(UserRegisterRequestDto userRegisterRequest) {
        if (userRepository.findByEmail(userRegisterRequest.email()).isPresent()) {
            throw new RegistrationException("Unable to complete registration");
        }

        String hashedPassword = passwordEncoder.encode(userRegisterRequest.password());
        Role adminRole = roleRepository.getRoleByName(Role.RoleName.ROLE_ADMIN);

        User user = userMapper.toModel(userRegisterRequest);
        user.setPassword(hashedPassword);
        user.setRoles(Set.of(adminRole));
        User savedUser = userRepository.save(user);

        return userMapper.toDto(savedUser);
    }
}
