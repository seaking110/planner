package com.example.planner.service;

import com.example.planner.dto.PlannerRequestDto;
import com.example.planner.dto.PlannerResponseDto;
import com.example.planner.entity.Planner;
import com.example.planner.repository.PlannerRepository;
import org.springframework.stereotype.Service;

@Service
public class PlannerServiceImpl implements PlannerService {

    private final PlannerRepository plannerRepository;

    public PlannerServiceImpl(PlannerRepository plannerRepository){
        this.plannerRepository = plannerRepository;
    }
    @Override
    public PlannerResponseDto savePlanner(PlannerRequestDto dto) {
        return plannerRepository.savedPlanner(dto.getTask(), dto.getWriter(), dto.getPassword());
    }
}
