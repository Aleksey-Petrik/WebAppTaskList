package ru.tasklist.springboot.business.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.tasklist.springboot.business.entity.Task;
import ru.tasklist.springboot.business.service.TaskService;

import java.util.List;

@Log4j2
@RestController
@RequestMapping("/task")
public class TaskController {

    private final TaskService service;

    @Autowired
    public TaskController(TaskService service) {
        this.service = service;
    }

    @PostMapping("/all")
    public ResponseEntity<List<Task>> findAll(@RequestBody String email) {
        log.info("POST get all tasks for email - {}", email);
        return ResponseEntity.ok(service.findAll(email));
    }

    @PostMapping("/id")
    public ResponseEntity<Task> findById(@RequestBody Long taskId) {
        log.info("POST search for id priorities - {}", taskId);
        return service.findById(taskId).map(ResponseEntity::ok)
                .orElseGet(() -> new ResponseEntity(String.format("Task with id - %d not found", taskId), HttpStatus.NOT_ACCEPTABLE));
    }

    @PutMapping
    public ResponseEntity<Task> add(@RequestBody Task task) {
        log.info("Put new task {}", task);

        if (task.getId() != null && task.getId() != 0) {
            return new ResponseEntity("Id mast be empty", HttpStatus.NOT_ACCEPTABLE);
        }

        if (task.getTitle() == null || task.getTitle().isEmpty() || task.getTitle().isBlank()) {
            return new ResponseEntity("Title not mast be empty", HttpStatus.NOT_ACCEPTABLE);
        }

        return ResponseEntity.ok(service.add(task));
    }

    @PatchMapping("/update")
    public ResponseEntity<Task> update(@RequestBody Task task) {
        log.info("Update task {}", task);

        if (task.getId() == null || task.getId() == 0) {
            return new ResponseEntity("Id not mast be empty", HttpStatus.NOT_ACCEPTABLE);
        }

        if (task.getTitle() == null || task.getTitle().isEmpty() || task.getTitle().isBlank()) {
            return new ResponseEntity("Title not mast be empty", HttpStatus.NOT_ACCEPTABLE);
        }

        return ResponseEntity.ok(service.update(task));
    }

    @DeleteMapping("/delete")
    public ResponseEntity delete(@RequestBody Long taskId) {
        log.info("Delete task Id - {}", taskId);

        try {
            service.deleteById(taskId);
        } catch (EmptyResultDataAccessException e) {
            return new ResponseEntity("Task not found", HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

}