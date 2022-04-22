package ru.tasklist.springboot.auth.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.tasklist.springboot.auth.entity.User;
import ru.tasklist.springboot.auth.service.UserService;

import javax.validation.Valid;

@Log4j2
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService service;

    @Autowired
    public AuthController(UserService service) {
        this.service = service;
    }

    @PostMapping("/id")
    public ResponseEntity<User> findById(@RequestBody Long userId) {
        log.info("Get user by id - {}", userId);
        return ResponseEntity.ok(service.findById(userId).get());
    }

    @PutMapping("/register")
    public ResponseEntity register(@Valid @RequestBody User user) {
        log.info("Register new user {}", user);
        return service.save(user) != null ? ResponseEntity.ok().build() : new ResponseEntity("Error in registers.", HttpStatus.NOT_ACCEPTABLE);
    }
}
