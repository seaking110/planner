package com.example.plan.repository;

import com.example.plan.dto.PlanResponseDto;
import com.example.plan.entity.Plan;
import org.springframework.data.domain.Page;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface PlanRepository {
    PlanResponseDto save(String task, Long user_id, String password, String writer);

    Optional<Plan> findPlanById(Long id);


    Page<Plan> findPlans(Date updated_at, String writer, int page, int size);

    int updatePlan(Long id, String task, Long user_id);

    String findPassWordById(Long id);

    int deletePlan(Long id);

//    List<PlanResponseDto> findPlannersByPage(int page_num, int page_size);
}