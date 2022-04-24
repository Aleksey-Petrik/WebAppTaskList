package ru.tasklist.springboot.auth.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.tasklist.springboot.auth.entity.Activity;

@Repository
public interface ActivityRepository extends CrudRepository<Activity, Long> {
}
