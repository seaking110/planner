package com.example.planner.repository;

import com.example.planner.dto.PlannerResponseDto;
import com.example.planner.entity.Planner;

public interface PlannerRepository {
    public PlannerResponseDto savedPlanner(String task, String writer, String password);
}