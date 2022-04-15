package ru.tasklist.springboot.business.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.tasklist.springboot.business.entity.Priority;

import java.util.List;

public interface PriorityRepository extends JpaRepository<Priority, Long> {

    @Query("SELECT priority FROM Priority priority WHERE " +
            "(:title IS NULL OR :title = '' " +
            "OR LOWER(priority.title) LIKE LOWER(CONCAT('%', :title, '%') ))" +
            "AND priority.user.email = :email " +
            "ORDER BY priority.title ASC")
    List<Priority> find(@Param("title") String title, @Param("email") String email);

    List<Priority> findByUserEmailOrderByTitleAsc(String email);

}
