package com.example.planner.controller;

import com.example.planner.dto.PlannerRequestDto;
import com.example.planner.dto.PlannerResponseDto;
import com.example.planner.service.PlannerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/planner")
public class PlannerController {
    private final PlannerService plannerService;

    public PlannerController(PlannerService plannerService){
        this.plannerService = plannerService;
    }

    @PostMapping
    public ResponseEntity<PlannerResponseDto> createPlanner(@RequestBody PlannerRequestDto dto){

        return new ResponseEntity<>(plannerService.savePlanner(dto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlannerResponseDto> findPlannerById(@PathVariable Long id) {
        return new ResponseEntity<>(plannerService.findPlannerById(id),HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<PlannerResponseDto>> findAllPlanners() {
        return new ResponseEntity<>(plannerService.findAllPlanners(),HttpStatus.OK);
    }


}
