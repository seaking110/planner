package com.example.planner.controller;

import com.example.planner.dto.PlannerRequestDto;
import com.example.planner.dto.PlannerResponseDto;
import com.example.planner.service.PlannerService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/planner")
public class PlannerController {
    private final PlannerService plannerService;

    // 생성자 의존성 주입을 통해 Service와 연결
    public PlannerController(PlannerService plannerService){
        this.plannerService = plannerService;
    }

    // 일정 생성
    @PostMapping
    public ResponseEntity<PlannerResponseDto> createPlanner(@RequestBody @Valid PlannerRequestDto dto){

        return new ResponseEntity<>(plannerService.savePlanner(dto), HttpStatus.CREATED);
    }

    // 일정 조회
    @GetMapping("/{id}")
    public ResponseEntity<PlannerResponseDto> findPlannerById(@PathVariable Long id) {
        return new ResponseEntity<>(plannerService.findPlannerById(id),HttpStatus.OK);
    }

    // 모든 일정 조회
    @GetMapping
    public ResponseEntity<List<PlannerResponseDto>> findAllPlanners(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date updated_at,
            @RequestParam(required = false) String writer
            ) {
        return new ResponseEntity<>(plannerService.findAllPlanners(updated_at, writer),HttpStatus.OK);
    }

    // 일정 수정
    @PutMapping("/{id}")
    public ResponseEntity<PlannerResponseDto> updatePlanner(@PathVariable Long id, @RequestBody PlannerRequestDto dto) {
        return new ResponseEntity<>(plannerService.updatePlanner(id, dto),HttpStatus.OK);
    }

    //일정 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlanner(@PathVariable Long id, @RequestBody PlannerRequestDto dto) {
        plannerService.deletePlanner(id, dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //일정 페이징 조회
    @GetMapping("/page")
    public ResponseEntity<List<PlannerResponseDto>> findPlannersByPage(
            @RequestParam int page_num,
            @RequestParam int page_size
    ) {
        return new ResponseEntity<>(plannerService.findPlannersByPage(page_num, page_size),HttpStatus.OK);
    }
}
