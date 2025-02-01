package com.example.planner.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@Getter
@AllArgsConstructor
public class PlannerResponseDto {
    private Long id;
    private String task;
    private String writer;
    private Date createdAt;
    private Date updatedAt;
}
