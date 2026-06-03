package com.jobscape.entitymanager.controller;

import com.jobscape.entitymanager.model.Application;
import com.jobscape.entitymanager.repository.ApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/applications")
public class ApplicationController {
    @Autowired
    ApplicationRepository applicationRepository;

    @GetMapping("/{id}")
    public Application getApplicationById(@PathVariable Long id) {
        return applicationRepository.findById(id).orElse(null);
    }

    @GetMapping
    public List<Application> findAll() {
        return applicationRepository.findAll();
    }
}
