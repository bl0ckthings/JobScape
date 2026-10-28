package com.jobscape.applicationservice.controller;

import com.jobscape.applicationservice.dto.applicationstep.ApplicationStepDto;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/application-service/steps")
public class ApplicationStepController {

    @GetMapping("/{id}")
    public ApplicationStepDto getApplicationStepById(@PathVariable Long id) {
        return null;
    }

    @GetMapping()
    public List<ApplicationStepDto> getByApplicationId(@RequestParam Long applicationId) {
        return null;
    }

    @PostMapping("/create")
    public ApplicationStepDto create(@RequestBody ApplicationStepDto applicationStepDto) {
        return null;
    }

    @PutMapping("/update/{id}")
    public  ApplicationStepDto update(@PathVariable Long id, @RequestBody ApplicationStepDto updatedAppStepDto) {
        return null;
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Long id) {
    }
}
