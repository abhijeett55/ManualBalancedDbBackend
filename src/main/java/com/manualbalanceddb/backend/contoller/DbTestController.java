package com.manualbalanceddb.backend.contoller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.manualbalanceddb.backend.repository.PartitionRepository;

@RestController
@RequestMapping("/db")
public class DbTestController {

    private final PartitionRepository repository;

    public DbTestController(PartitionRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/count")
    public long count() {
        return repository.count();
    }
}