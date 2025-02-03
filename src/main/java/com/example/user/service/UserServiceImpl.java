package com.example.user.service;


import com.example.user.dto.UserRequestDto;
import com.example.user.dto.UserResponseDto;
import com.example.user.repository.UserRepository;
import org.springframework.stereotype.Service;

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
    public String findWriterById(Long userId) {
        return userRepository.findWriterById(userId);
    }

}
