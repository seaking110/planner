package com.example.planner.dto;

import lombok.Getter;

@Getter
public class PlannerRequestDto {
    private String task;
    private Long user_id;
    private String password;
}
