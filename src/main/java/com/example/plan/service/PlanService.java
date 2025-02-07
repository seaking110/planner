package com.example.plan.service;

import com.example.plan.dto.PlanDeleteRequestDto;
import com.example.plan.dto.PlanSaveRequestDto;
import com.example.plan.dto.PlanResponseDto;
import com.example.plan.dto.PlanUpdateRequestDto;
import org.springframework.data.domain.Page;

import java.util.Date;
import java.util.List;

public interface PlanService {
    PlanResponseDto savePlan(PlanSaveRequestDto dto);

    PlanResponseDto getPlanById(Long id);

    Page<PlanResponseDto> findPlans(Date updated_at, String writer, int page, int size);

    PlanResponseDto updatePlan(Long id, PlanUpdateRequestDto dto);

    void deletePlan(Long id, PlanDeleteRequestDto dto);

//    List<PlanResponseDto> findPlannersByPage(int page_num, int page_size);
}
