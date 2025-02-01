package com.example.planner.service;

import com.example.planner.dto.PlannerRequestDto;
import com.example.planner.dto.PlannerResponseDto;

import java.util.List;

public interface PlannerService {
    PlannerResponseDto savePlanner(PlannerRequestDto dto);

    PlannerResponseDto findPlannerById(Long id);

    List<PlannerResponseDto> findAllPlanners();
}
