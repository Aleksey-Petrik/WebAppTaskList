package ru.tasklist.springboot.business.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.tasklist.springboot.business.entity.Task;
import ru.tasklist.springboot.business.repository.TaskRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TaskService {

    private final TaskRepository repository;

    @Autowired
    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    public List<Task> findAll(String email) {
        return repository.findByUserEmailOrderByTitleAsc(email);
    }

    public Task add(Task task) {
        return repository.save(task);
    }

    public Task update(Task task) {
        return repository.save(task);
    }

    public void deleteById(Long taskId) {
        repository.deleteById(taskId);
    }

    public Optional<Task> findById(Long taskId) {
        return repository.findById(taskId);
    }

}