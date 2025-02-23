package com.example.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.Date;


@Getter
@AllArgsConstructor
public class UserResponseDto {
    private final Long user_id;
    private final String writer;
    private final String email;
    private final Date createdAt;
    private final Date updatedAt;
}
