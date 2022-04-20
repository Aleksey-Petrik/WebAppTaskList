package ru.tasklist.springboot.business.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.tasklist.springboot.business.entity.Priority;
import ru.tasklist.springboot.business.search.PrioritySearchValues;
import ru.tasklist.springboot.business.service.PriorityService;

import java.util.List;

@Log4j2
@RestController
@RequestMapping("/priority")
public class PriorityController {
    private final PriorityService service;

    @Autowired
    public PriorityController(PriorityService service) {
        this.service = service;
    }

    @PostMapping("/all")
    public List<Priority> findAll(@RequestBody String email) {
        log.info("POST get all priorities for email - {}", email);
        return service.findAll(email);
    }

    @PostMapping("/search")
    public ResponseEntity<List<Priority>> findByParams(@RequestBody PrioritySearchValues prioritySearchValues) {
        log.info("POST search criteria priorities - {}", prioritySearchValues);
        List<Priority> priorities = service.find(prioritySearchValues.getTitle(), prioritySearchValues.getEmail());
        return ResponseEntity.ok(priorities);
    }

    @PostMapping("/id")
    public ResponseEntity<Priority> findById(@RequestBody Long priorityId) {
        log.info("POST search for id priorities - {}", priorityId);
        return service.findById(priorityId).map(ResponseEntity::ok)
                .orElseGet(() -> new ResponseEntity(String.format("Priority with id = %d not found.", priorityId), HttpStatus.NOT_FOUND));
    }

    @PutMapping("/add")
    public ResponseEntity<Priority> add(@RequestBody Priority priority) {
        log.info("Put new priority {}", priority);

        if (priority.getId() != null && priority.getId() != 0) {
            return new ResponseEntity("Id mast be empty", HttpStatus.NOT_ACCEPTABLE);
        }
        if (priority.getTitle().isEmpty() || priority.getTitle().isBlank()) {
            return new ResponseEntity("Title not be empty", HttpStatus.NOT_ACCEPTABLE);
        }
        if (priority.getColor().isEmpty() || priority.getColor().isBlank()) {
            return new ResponseEntity("Color not be empty", HttpStatus.NOT_ACCEPTABLE);
        }

        return ResponseEntity.ok(service.add(priority));
    }

    @PatchMapping("/update")
    public ResponseEntity<Priority> update(@RequestBody Priority priority) {
        log.info("Update priority {}", priority);

        if (priority.getId() == null || priority.getId() == 0) {
            return new ResponseEntity("Id not be empty", HttpStatus.NOT_ACCEPTABLE);
        }

        if (priority.getTitle().isEmpty() || priority.getTitle().isBlank()) {
            return new ResponseEntity("Title not be empty", HttpStatus.NOT_ACCEPTABLE);
        }

        if (priority.getColor().isEmpty() || priority.getColor().isBlank()) {
            return new ResponseEntity("Color not be empty", HttpStatus.NOT_ACCEPTABLE);
        }

        return ResponseEntity.ok(service.update(priority));
    }

    @DeleteMapping("/delete")
    public ResponseEntity delete(@RequestBody Long priorityId) {
        log.info("Delete priority Id - {}", priorityId);

        try {
            service.delete(priorityId);
        } catch (EmptyResultDataAccessException e) {
            return new ResponseEntity("Id not found", HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity(HttpStatus.OK);
    }
}
