package com.example.user.entity;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;
@Getter
@AllArgsConstructor
public class User {
    private Long user_id;              // ID
    private String writer;        // 작성자명
    private String email;      // 이메일
    private Date createdAt;  // 등록일
    private Date updatedAt;  // 수정일
}
