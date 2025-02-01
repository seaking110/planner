package com.example.planner.service;

import com.example.planner.dto.PlannerRequestDto;
import com.example.planner.dto.PlannerResponseDto;
import com.example.planner.entity.Planner;
import com.example.planner.repository.PlannerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

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

    @Override
    public PlannerResponseDto findPlannerById(Long id) {
        Optional<Planner> optionalPlanner = plannerRepository.findPlannerById(id);
        if (optionalPlanner.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return new PlannerResponseDto(optionalPlanner.get().getId(), optionalPlanner.get().getTask(),
                optionalPlanner.get().getWriter(), optionalPlanner.get().getCreatedAt(), optionalPlanner.get().getUpdatedAt());
    }

    @Override
    public List<PlannerResponseDto> findAllPlanners() {
        return plannerRepository.findAllPlanners();
    }
}
