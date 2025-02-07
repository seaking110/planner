package com.example.user.service;


import com.example.user.dto.UserRequestDto;
import com.example.user.dto.UserResponseDto;
import com.example.user.entity.User;
import com.example.user.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    @Override
    public UserResponseDto saveUser(UserRequestDto dto) {
        return userRepository.savedUser(dto.getWriter(), dto.getEmail());
    }

    @Override
    public UserResponseDto getUserById(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "존재 하지 않는 유저입니다.");
        }
        return new UserResponseDto(user.get().getUserId(), user.get().getWriter(), user.get().getEmail(), user.get().getCreatedAt(), user.get().getUpdatedAt());
    }

}
