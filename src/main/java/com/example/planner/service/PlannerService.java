package com.example.planner.service;

import com.example.planner.dto.PlannerRequestDto;
import com.example.planner.dto.PlannerResponseDto;
import java.util.Date;
import java.util.List;

public interface PlannerService {
    PlannerResponseDto savePlanner(PlannerRequestDto dto);

    PlannerResponseDto findPlannerById(Long id);

    List<PlannerResponseDto> findAllPlanners(Date updated_at, String writer);

    PlannerResponseDto updatePlanner(Long id, PlannerRequestDto dto);

    void deletePlanner(Long id, PlannerRequestDto dto);

    List<PlannerResponseDto> findPlannersByPage(int page_num, int page_size);
}
