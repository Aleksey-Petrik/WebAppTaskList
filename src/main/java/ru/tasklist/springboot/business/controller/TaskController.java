package ru.tasklist.springboot.business.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.tasklist.springboot.business.entity.Task;
import ru.tasklist.springboot.business.search.TaskSearchValues;
import ru.tasklist.springboot.business.service.TaskService;

import java.util.Calendar;
import java.util.Date;
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

    @PostMapping("/search")
    public ResponseEntity<Page<Task>> findByParams(@RequestBody TaskSearchValues taskSearchValues) {
        log.info("POST search for params tasks - {}", taskSearchValues);

        String title = taskSearchValues.getTitle();
        Integer completed = taskSearchValues.getCompleted();
        Long priorityId = taskSearchValues.getPriorityId();
        Long categoryId = taskSearchValues.getCategoryId();

        String email = taskSearchValues.getEmail();

        Integer pageNumber = taskSearchValues.getPageNumber();
        Integer pageSize = taskSearchValues.getPageSize();

        String sortColumn = taskSearchValues.getSortColumn();
        String sortDirection = taskSearchValues.getSortDirection();

        Date dateFrom = null;
        Date dateTo = null;

        if (taskSearchValues.getDateFrom() != null) {
            Calendar calendarFrom = Calendar.getInstance();
            calendarFrom.setTime(taskSearchValues.getDateFrom());
            calendarFrom.set(Calendar.HOUR_OF_DAY, 0);
            calendarFrom.set(Calendar.MINUTE, 0);
            calendarFrom.set(Calendar.SECOND, 0);
            calendarFrom.set(Calendar.MILLISECOND, 0);
            dateFrom = calendarFrom.getTime();
        }

        if (taskSearchValues.getDateTo() != null) {
            Calendar calendarTo = Calendar.getInstance();
            calendarTo.setTime(taskSearchValues.getDateTo());
            calendarTo.set(Calendar.HOUR_OF_DAY, 23);
            calendarTo.set(Calendar.MINUTE, 59);
            calendarTo.set(Calendar.SECOND, 59);
            calendarTo.set(Calendar.MILLISECOND, 999);
            dateTo = calendarTo.getTime();
        }

        Sort.Direction direction = sortDirection == null
                || sortDirection.trim().length() == 0
                || sortDirection.trim().equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;

        Sort sort = Sort.by(direction, sortColumn, "id");

        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, sort);

        Page<Task> page = service.findByParams(title, completed, priorityId, categoryId, email, dateFrom, dateTo, pageRequest);

        return ResponseEntity.ok(page);
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