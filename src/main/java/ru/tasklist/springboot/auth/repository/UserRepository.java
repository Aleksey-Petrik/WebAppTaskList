package ru.tasklist.springboot.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.tasklist.springboot.auth.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
