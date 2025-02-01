package com.example.planner.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@Getter
@AllArgsConstructor
public class Planner {
    private Long id;              // ID
    private String task;          // 할 일
    private String writer;        // 작성자명
    private String password;      // 비밀번호
    private Date createdAt;  // 작성일
    private Date updatedAt;  // 수정일
}
