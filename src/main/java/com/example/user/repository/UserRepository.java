package com.example.user.repository;

import com.example.planner.dto.PlannerResponseDto;
import com.example.planner.entity.Planner;
import com.example.user.dto.UserResponseDto;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface UserRepository {
    UserResponseDto savedUser(String writer, String email);


    String findWriterById(Long user_id);

}