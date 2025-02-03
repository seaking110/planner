package com.example.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.Date;


@Getter
@AllArgsConstructor
public class UserResponseDto {
    private Long user_id;
    private String writer;
    private String email;
    private Date createdAt;
    private Date updatedAt;
}
