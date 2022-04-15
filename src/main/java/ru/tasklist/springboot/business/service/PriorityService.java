package ru.tasklist.springboot.business.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.tasklist.springboot.business.entity.Priority;
import ru.tasklist.springboot.business.repository.PriorityRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PriorityService {

    private final PriorityRepository repository;

    @Autowired
    public PriorityService(PriorityRepository repository) {
        this.repository = repository;
    }

    public List<Priority> findAll(String email) {
        return repository.findByUserEmailOrderByTitleAsc(email);
    }

    public List<Priority> find(String title, String email) {
        return repository.find(title, email);
    }

    public Optional<Priority> findById(Long categoryId) {
        return repository.findById(categoryId);
    }

    public Priority add(Priority priority) {
        return repository.save(priority);
    }

    public Priority update(Priority priority) {
        return repository.save(priority);
    }

    public void delete(Long priorityId) {
        repository.deleteById(priorityId);
    }
}