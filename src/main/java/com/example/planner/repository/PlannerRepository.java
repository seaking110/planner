package com.example.planner.repository;

import com.example.planner.dto.PlannerResponseDto;
import com.example.planner.entity.Planner;

import java.util.List;
import java.util.Optional;

public interface PlannerRepository {
    PlannerResponseDto savedPlanner(String task, String writer, String password);

    Optional<Planner> findPlannerById(Long id);

    List<PlannerResponseDto> findAllPlanners();
}