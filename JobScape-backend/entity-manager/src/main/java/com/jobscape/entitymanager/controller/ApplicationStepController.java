package com.jobscape.entitymanager.controller;

import com.jobscape.entitymanager.model.ApplicationStep;
import com.jobscape.entitymanager.repository.ApplicationStepRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/data/steps")
public class ApplicationStepController {
    @Autowired
    ApplicationStepRepository applicationStepRepository;

    @GetMapping("/application/{id}")
    public List<ApplicationStep> getStepsByApplicationId(@PathVariable Long id) {
        return applicationStepRepository.findByApplicationId(id);
    }



    @PostMapping("/create")
    public ApplicationStep createStep(@RequestBody ApplicationStep step) {
        return applicationStepRepository.save(step);
    }

    @DeleteMapping("/delete")
    public void DeleteStep(@RequestBody Long id) {
        applicationStepRepository.deleteById(id);
    }

    @PutMapping("/update/{id}")
    public ApplicationStep updateStep(@PathVariable Long id ,@RequestBody ApplicationStep updatedStep) {
        ApplicationStep existing = applicationStepRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Step not found"));

        if (updatedStep.getComment() != null) {
            existing.setComment(updatedStep.getComment());
        }
        if (updatedStep.getStepDate() != null) {
            existing.setStepDate(updatedStep.getStepDate());
        }
        if (updatedStep.getStepStatus() != null) {
            existing.setStepStatus(updatedStep.getStepStatus());
        }
        return applicationStepRepository.save(existing);
    }



}
