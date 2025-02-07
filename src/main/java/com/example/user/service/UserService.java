package com.example.user.service;

import com.example.user.dto.UserRequestDto;
import com.example.user.dto.UserResponseDto;

public interface UserService {
    UserResponseDto saveUser(UserRequestDto dto);

    UserResponseDto getUserById(Long userId);


}
