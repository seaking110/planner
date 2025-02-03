package com.example.planner.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class PlannerRequestDto {
    @NotBlank
    @Size(max = 200)
    private String task;
    private Long user_id;
    @NotBlank
    private String password;
}
