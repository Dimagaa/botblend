package com.app.botblend.service.impl;

import com.app.botblend.dto.user.UserDto;
import com.app.botblend.dto.user.UserRegisterRequestDto;
import com.app.botblend.exception.EntityNotFoundException;
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
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    @Override
    public UserDto updateCurrentUser(UserRegisterRequestDto requestDto) {
        String hashedPassword = passwordEncoder.encode(requestDto.password());

        User currentUser = userRepository.getCurrentUser();
        currentUser.setEmail(requestDto.email());
        currentUser.setPassword(hashedPassword);
        currentUser.setFirstName(requestDto.firstName());
        currentUser.setLastName(requestDto.lastName());
        User updatedUser = userRepository.save(currentUser);

        return userMapper.toDto(updatedUser);
    }

    @Override
    public UserDto getCurrent() {
        return userMapper.toDto(userRepository.getCurrentUser());
    }

    @Transactional
    @Override
    public void delete(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Cannot delete: User not found with id: " + id
                ));
        userRepository.delete(user);
    }
}
