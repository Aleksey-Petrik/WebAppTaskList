package ru.tasklist.springboot.business.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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

}
