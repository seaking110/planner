package com.example.planner.service;

import com.example.planner.dto.PlannerRequestDto;
import com.example.planner.dto.PlannerResponseDto;
import com.example.planner.entity.Planner;
import com.example.planner.repository.PlannerRepository;
import com.example.user.service.UserService;
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
    private final UserService userService;

    // 생성자 의존성 주입을 통해 Repository와 연결
    public PlannerServiceImpl(PlannerRepository plannerRepository, UserService userService){
        this.plannerRepository = plannerRepository;
        this.userService = userService;
    }

    //일정 생성
    @Override
    public PlannerResponseDto savePlanner(PlannerRequestDto dto) {
        String writer = userService.findWriterById(dto.getUser_id());
        if (writer.equals("")) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "존재 하지 않는 유저입니다.");
        }
        return plannerRepository.savedPlanner(dto.getTask(), dto.getUser_id(), dto.getPassword(),writer);
    }

    // 일정 조회
    @Override
    public PlannerResponseDto findPlannerById(Long id) {
        Optional<Planner> optionalPlanner = plannerRepository.findPlannerById(id);
        if (optionalPlanner.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "존재 하지 않는 일정입니다.");
        }
        // user 테이블에 존재하는 writer를 가져오기 위해 유저 서비스와 연결
        String writer = userService.findWriterById(optionalPlanner.get().getUser_id());
        if (writer.equals("")) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "존재 하지 않는 유저입니다.");
        }
        return new PlannerResponseDto(optionalPlanner.get().getId(), optionalPlanner.get().getTask(),
                writer, optionalPlanner.get().getCreatedAt(), optionalPlanner.get().getUpdatedAt());
    }

    // 일정 전체 조회
    @Override
    public List<PlannerResponseDto> findAllPlanners(Date updated_at, String writer) {
        return plannerRepository.findAllPlanners(updated_at, writer);
    }

    // 일정 수정
    @Transactional
    @Override
    public PlannerResponseDto updatePlanner(Long id, PlannerRequestDto dto) {
        // 필수 정보 및 비밀번호 확인 후 수정 진행
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

        // 역시 이름을 반납해야 하므로 유저 서비스와 연결하여 이름 가져오기
        String writer = userService.findWriterById(optionalPlanner.get().getUser_id());
        if (writer.equals("")) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "존재 하지 않는 유저입니다.");
        }
        return new PlannerResponseDto(optionalPlanner.get().getId(), optionalPlanner.get().getTask(),
                writer, optionalPlanner.get().getCreatedAt(), optionalPlanner.get().getUpdatedAt());
    }
    @Transactional
    @Override
    public void deletePlanner(Long id, PlannerRequestDto dto) {
        // 비밀번호 확인 후 같으면 일정 삭제
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

    // 페이징 유저 조회
    @Override
    public List<PlannerResponseDto> findPlannersByPage(int page_num, int page_size) {
        return plannerRepository.findPlannersByPage(page_num, page_size);
    }
}
