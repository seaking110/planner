package com.example.plan.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class PlanDeleteRequestDto {
    @NotBlank
    private String password;
}
