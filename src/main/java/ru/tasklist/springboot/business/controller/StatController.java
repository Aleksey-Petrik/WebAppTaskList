package ru.tasklist.springboot.business.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.tasklist.springboot.business.entity.Stat;
import ru.tasklist.springboot.business.service.StatService;

@Log4j2
@RestController
@RequestMapping("/stat")
public class StatController {

    private final StatService service;

    @Autowired
    public StatController(StatService service) {
        this.service = service;
    }

    @PostMapping("/stat")
    public ResponseEntity<Stat> find(@RequestBody String email) {
        log.info("POST get statistics for email - {}", email);
        return ResponseEntity.ok(service.findStat(email));
    }
}
