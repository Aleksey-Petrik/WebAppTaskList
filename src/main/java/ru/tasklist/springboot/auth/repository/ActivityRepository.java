package ru.tasklist.springboot.auth.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.tasklist.springboot.auth.entity.Activity;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface ActivityRepository extends CrudRepository<Activity, Long> {
    @Modifying
    @Transactional
    @Query("UPDATE Activity a SET a.activated = :active WHERE a.uuid = :uuid")
    int changeActivated(@Param("active") boolean active, @Param("uuid") String uuid);

    Optional<Activity> findByUserId(Long userId);

    Optional<Activity> findByUuid(String uuid);
}
