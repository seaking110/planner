package com.example.planner.repository;

import com.example.planner.dto.PlannerResponseDto;
import com.example.planner.entity.Planner;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface PlannerRepository {
    PlannerResponseDto savedPlanner(String task, Long user_id, String password, String writer);

    Optional<Planner> findPlannerById(Long id);


    List<PlannerResponseDto> findAllPlanners(Date updated_at, String writer);

    int updatePlanner(Long id, String task, Long user_id);

    String findPassWordById(Long id);

    int deletePlanner(Long id);

    List<PlannerResponseDto> findPlannersByPage(int page_num, int page_size);
}