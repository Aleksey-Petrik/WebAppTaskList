package ru.tasklist.springboot.business.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.tasklist.springboot.business.entity.Category;
import ru.tasklist.springboot.business.search.CategorySearchValues;
import ru.tasklist.springboot.business.service.CategoryService;

import java.util.List;
import java.util.Optional;

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

    @PostMapping("/search")
    public ResponseEntity<List<Category>> search(@RequestBody CategorySearchValues categorySearchValues) {
        log.info("POST search criteria categories - {}", categorySearchValues);
        List<Category> categories = service.findByTitle(categorySearchValues.getTitle(), categorySearchValues.getEmail());
        return ResponseEntity.ok(categories);
    }

    @PostMapping("/id")
    public ResponseEntity<Category> search(@RequestBody Long categoryId) {
        log.info("POST search for id categories - {}", categoryId);

        Optional<Category> optionalCategory = service.findById(categoryId);
        return optionalCategory.map(ResponseEntity::ok)
                .orElseGet(() -> new ResponseEntity(String.format("Category with id = %d not found.", categoryId), HttpStatus.NOT_FOUND));
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

    @DeleteMapping("/delete")
    public ResponseEntity delete(@RequestBody Long categoryId) {
        log.info("Delete category Id - {}", categoryId);

        try {
            service.delete(categoryId);
        } catch (EmptyResultDataAccessException e) {
            return new ResponseEntity("Id not found", HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity(HttpStatus.OK);
    }
}
