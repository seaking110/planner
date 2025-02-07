package com.example.plan.service;

import com.example.plan.dto.PlanDeleteRequestDto;
import com.example.plan.dto.PlanSaveRequestDto;
import com.example.plan.dto.PlanResponseDto;
import com.example.plan.dto.PlanUpdateRequestDto;
import com.example.plan.entity.Plan;
import com.example.plan.repository.PlanRepository;
import com.example.user.entity.User;
import com.example.user.service.UserService;
import org.springframework.context.ApplicationContextException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.util.Date;
import java.util.List;

@Service
public class PlanServiceImpl implements PlanService {

    private final PlanRepository planRepository;
    private final UserService userService;

    // 생성자 의존성 주입을 통해 Repository와 연결
    public PlanServiceImpl(PlanRepository planRepository, UserService userService){
        this.planRepository = planRepository;
        this.userService = userService;
    }

    //일정 생성
    @Override
    public PlanResponseDto savePlan(PlanSaveRequestDto dto) {
        return planRepository.save(dto.getTask(), dto.getUser_id(), dto.getPassword(), userService.getUserById(dto.getUser_id()).getWriter());
    }

    // 일정 조회
    @Transactional(readOnly= true)
    @Override
    public PlanResponseDto getPlanById(Long id) {
        Plan plan = planRepository.findPlanById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재 하지 않는 일정입니다."));
        return new PlanResponseDto(plan.getId(), plan.getTask(),
                userService.getUserById(plan.getUserId()).getWriter(), plan.getCreatedAt(), plan.getUpdatedAt());
    }

    // 일정 전체 조회
    @Transactional(readOnly= true)
    @Override
    public Page<PlanResponseDto> findPlans(Date updated_at, String writer, int page, int size) {
        Page<Plan> plansPage = planRepository.findPlans(updated_at, writer, page, size);
        return plansPage.map(plan -> {
            return new PlanResponseDto(
                    plan.getId(),
                    plan.getTask(),
                    userService.getUserById(plan.getUserId()).getWriter(),
                    plan.getCreatedAt(),
                    plan.getUpdatedAt()
            );
        });
    }

    // 일정 수정
    @Transactional
    @Override
    public PlanResponseDto updatePlan(Long id, PlanUpdateRequestDto dto) {
        // 필수 정보 및 비밀번호 확인 후 수정 진행
        if (dto.getTask() == null || dto.getUser_id() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "필수 정보가 빠졌습니다.");
        }
        String password = planRepository.findPassWordById(id);
        if (!isPasswordMatching(password, dto.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "비밀번호가 틀렸습니다.");
        }
        int updateRow = planRepository.updatePlan(id, dto.getTask(), dto.getUser_id());
        if (updateRow == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 일정입니다.");
        }

        Plan plan = planRepository.findPlanById(id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 일정입니다."));

        return new PlanResponseDto(plan.getId(), plan.getTask(),
                userService.getUserById(plan.getUserId()).getWriter(), plan.getCreatedAt(), plan.getUpdatedAt());
    }
    @Transactional
    @Override
    public void deletePlan(Long id, PlanDeleteRequestDto dto) {
        // 비밀번호 확인 후 같으면 일정 삭제
        if (dto.getPassword() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "필수 정보가 빠졌습니다.");
        }
        String password = planRepository.findPassWordById(id);
        if (!isPasswordMatching(password, dto.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "비밀번호가 틀렸습니다.");
        }

        int deletedRow = planRepository.deletePlan(id);
        if (deletedRow == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "존재 하지 않는 일정입니다.");
        }
    }

    //비밀번호 체크
    public boolean isPasswordMatching(String originalPassword, String confirmPassword) {
        return originalPassword.equals(confirmPassword);
    }

//    // 페이징 유저 조회
//    @Transactional(readOnly= true)
//    @Override
//    public List<PlanResponseDto> findPlannersByPage(int page_num, int page_size) {
//        return planRepository.findPlannersByPage(page_num, page_size);
//    }
}
