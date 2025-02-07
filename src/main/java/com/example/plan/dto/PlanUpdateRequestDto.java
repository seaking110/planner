package com.example.plan.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class PlanUpdateRequestDto {
    @NotBlank
    @Size(max = 200)
    private String task;
    private Long user_id;
    @NotBlank
    private String password;
}
