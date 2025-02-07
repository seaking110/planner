package com.example.user.repository;

import com.example.user.dto.UserResponseDto;
import com.example.user.entity.User;

import java.util.Optional;

public interface UserRepository {
    UserResponseDto savedUser(String writer, String email);

    Optional<User> findById(Long userId);

}