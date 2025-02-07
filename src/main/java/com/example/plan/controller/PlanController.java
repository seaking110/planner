package com.example.plan.controller;

import com.example.plan.dto.PlanDeleteRequestDto;
import com.example.plan.dto.PlanSaveRequestDto;
import com.example.plan.dto.PlanResponseDto;
import com.example.plan.dto.PlanUpdateRequestDto;
import com.example.plan.service.PlanService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/plans")
public class PlanController {
    private final PlanService planService;

    // 생성자 의존성 주입을 통해 Service와 연결
    public PlanController(PlanService planService){
        this.planService = planService;
    }

    // 일정 생성
    @PostMapping
    public ResponseEntity<PlanResponseDto> createPlan(@RequestBody @Valid PlanSaveRequestDto dto){

        return new ResponseEntity<>(planService.savePlan(dto), HttpStatus.CREATED);
    }

    // 일정 조회

    @GetMapping("/{id}")
    public ResponseEntity<PlanResponseDto> getPlanById(@PathVariable Long id) {
        return new ResponseEntity<>(planService.getPlanById(id),HttpStatus.OK);
    }


    // 모든 일정 조회
    @GetMapping
    public ResponseEntity<Page<PlanResponseDto>> findPlans(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date updated_at,
            @RequestParam(required = false) String writer,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size
            ) {
        return new ResponseEntity<>(planService.findPlans(updated_at, writer, page - 1, size),HttpStatus.OK);
    }

    // 일정 수정
    @PutMapping("/{id}")
    public ResponseEntity<PlanResponseDto> updatePlan(@PathVariable Long id, @RequestBody PlanUpdateRequestDto dto) {
        return new ResponseEntity<>(planService.updatePlan(id, dto),HttpStatus.OK);
    }

    //일정 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlan(@PathVariable Long id, @RequestBody PlanDeleteRequestDto dto) {
        planService.deletePlan(id, dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

//    //일정 페이징 조회
//    @GetMapping("/page")
//    public ResponseEntity<List<PlanResponseDto>> findPlannersByPage(
//            @RequestParam int page_num,
//            @RequestParam int page_size
//    ) {
//        return new ResponseEntity<>(planService.findPlannersByPage(page_num, page_size),HttpStatus.OK);
//    }
}
