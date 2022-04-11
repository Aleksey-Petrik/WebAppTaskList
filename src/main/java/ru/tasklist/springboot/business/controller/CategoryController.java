package ru.tasklist.springboot.business.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.tasklist.springboot.business.entity.Category;
import ru.tasklist.springboot.business.service.CategoryService;

import java.util.List;

@Log4j2
@RestController
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService service;

    @Autowired
    public CategoryController(CategoryService service) {
        this.service = service;
    }

    @PostMapping("/all")
    public List<Category> findAll(@RequestBody String email) {
        log.info("POST get all categories for email - {}", email);
        return service.findAll(email);
    }

    @PutMapping("/add")
    public ResponseEntity<Category> add(@RequestBody Category category) {
        log.info("Put new category {}", category);

        if (category.getId() != null && category.getId() != 0) {
            return new ResponseEntity("Id mast be empty", HttpStatus.NOT_ACCEPTABLE);
        }

        if (category.getTitle().isEmpty() || category.getTitle().isBlank()) {
            return new ResponseEntity("Title not be empty", HttpStatus.NOT_ACCEPTABLE);
        }

        return ResponseEntity.ok(service.add(category));
    }

    @PatchMapping("/update")
    public ResponseEntity<Category> update(@RequestBody Category category) {
        log.info("Update category {}", category);

        if (category.getId() == null || category.getId() == 0) {
            return new ResponseEntity("Id not be empty", HttpStatus.NOT_ACCEPTABLE);
        }

        if (category.getTitle().isEmpty() || category.getTitle().isBlank()) {
            return new ResponseEntity("Title not be empty", HttpStatus.NOT_ACCEPTABLE);
        }

        return ResponseEntity.ok(service.update(category));
    }
}
