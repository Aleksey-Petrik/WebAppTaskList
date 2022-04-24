package ru.tasklist.springboot.auth.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.tasklist.springboot.auth.entity.Role;

import java.util.Optional;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {

    Optional<Role> findByName(String name);

}
