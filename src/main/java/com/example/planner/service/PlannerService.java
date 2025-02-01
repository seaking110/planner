package com.example.planner.service;

import com.example.planner.dto.PlannerRequestDto;
import com.example.planner.dto.PlannerResponseDto;

public interface PlannerService {
    public PlannerResponseDto savePlanner(PlannerRequestDto dto);
}
