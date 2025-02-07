package com.example.plan.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.Date;

@Getter
@AllArgsConstructor
public class PlanResponseDto {
    private final Long id;
    private final String task;
    private final String writer;
    private final Date createdAt;
    private final Date updatedAt;
}
