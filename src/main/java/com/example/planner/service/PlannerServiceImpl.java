package com.example.planner.service;

import com.example.planner.dto.PlannerRequestDto;
import com.example.planner.dto.PlannerResponseDto;
import com.example.planner.entity.Planner;
import com.example.planner.repository.PlannerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
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
        return plannerRepository.savedPlanner(dto.getTask(), dto.getUser_id(), dto.getPassword());
    }

    @Override
    public PlannerResponseDto findPlannerById(Long id) {
        Optional<Planner> optionalPlanner = plannerRepository.findPlannerById(id);
        if (optionalPlanner.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "존재 하지 않는 일정입니다.");
        }
        String writer = plannerRepository.findWriterById(optionalPlanner.get().getUser_id());
        return new PlannerResponseDto(optionalPlanner.get().getId(), optionalPlanner.get().getTask(),
                writer, optionalPlanner.get().getCreatedAt(), optionalPlanner.get().getUpdatedAt());
    }

    @Override
    public List<PlannerResponseDto> findAllPlanners(Date updated_at, String writer) {
        return plannerRepository.findAllPlanners(updated_at, writer);
    }
    @Transactional
    @Override
    public PlannerResponseDto updatePlanner(Long id, PlannerRequestDto dto) {
        if (dto.getTask() == null || dto.getUser_id() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "필수 정보가 빠졌습니다.");
        }
        String password = plannerRepository.findPassWordById(id);
        if (!dto.getPassword().equals(password)){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"비밀번호가 틀렸습니다.");
        }
        int updateRow = plannerRepository.updatePlanner(id, dto.getTask(), dto.getUser_id());
        if (updateRow == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 일정입니다.");
        }

        Optional<Planner> optionalPlanner = plannerRepository.findPlannerById(id);

        String writer = plannerRepository.findWriterById(optionalPlanner.get().getUser_id());
        return new PlannerResponseDto(optionalPlanner.get().getId(), optionalPlanner.get().getTask(),
                writer, optionalPlanner.get().getCreatedAt(), optionalPlanner.get().getUpdatedAt());
    }

    @Override
    public void deletePlanner(Long id, PlannerRequestDto dto) {
        if (dto.getPassword() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "필수 정보가 빠졌습니다.");
        }

        String password = plannerRepository.findPassWordById(id);
        if (!dto.getPassword().equals(password)){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "비밀번호가 틀렸습니다.");
        }

        int deletedRow = plannerRepository.deletePlanner(id);
        if (deletedRow == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "존재 하지 않는 일정입니다.");
        }
    }
}
