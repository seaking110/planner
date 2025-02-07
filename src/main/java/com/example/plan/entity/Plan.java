package com.example.plan.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.Date;

@Getter
@AllArgsConstructor
public class Plan {
    private Long id;              // ID
    private String task;          // 할 일
    private Long userId;        // 작성자 ID
    private String password;      // 비밀번호
    private Date createdAt;  // 작성일
    private Date updatedAt;  // 수정일
}
