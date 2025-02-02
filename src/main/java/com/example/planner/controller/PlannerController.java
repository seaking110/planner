package com.example.planner.controller;

import com.example.planner.dto.PlannerRequestDto;
import com.example.planner.dto.PlannerResponseDto;
import com.example.planner.service.PlannerService;
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
    public ResponseEntity<List<PlannerResponseDto>> findAllPlanners(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date updated_at,
            @RequestParam(required = false) String writer
            ) {
        return new ResponseEntity<>(plannerService.findAllPlanners(updated_at, writer),HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlannerResponseDto> updatePlanner(@PathVariable Long id, @RequestBody PlannerRequestDto dto) {
        return new ResponseEntity<>(plannerService.updatePlanner(id, dto),HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlanner(@PathVariable Long id, @RequestBody PlannerRequestDto dto) {
        plannerService.deletePlanner(id, dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
