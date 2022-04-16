package ru.tasklist.springboot.business.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.tasklist.springboot.business.entity.Stat;
import ru.tasklist.springboot.business.repository.StatRepository;

import javax.transaction.Transactional;

@Service
@Transactional
public class StatService {

    private final StatRepository repository;

    @Autowired
    public StatService(StatRepository repository) {
        this.repository = repository;
    }

    public Stat findStat(String email) {
        return repository.findByUserEmail(email);
    }

}