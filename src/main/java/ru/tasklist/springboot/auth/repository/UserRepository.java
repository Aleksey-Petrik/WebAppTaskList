package ru.tasklist.springboot.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.tasklist.springboot.auth.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN TRUE ELSE FALSE END FROM User u WHERE LOWER(u.email) = LOWER(:email) ")
    boolean existByEmail(@Param("email") String email);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN TRUE ELSE FALSE END FROM User u WHERE LOWER(u.username) = LOWER(:username)")
    boolean existByUsername(@Param("username") String username);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);
}
