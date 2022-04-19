package ru.tasklist.springboot.business.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.tasklist.springboot.business.entity.Task;

import java.util.Date;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query("SELECT task FROM Task task WHERE " +
            "(:title IS NULL OR :title = '' OR LOWER(task.title) LIKE LOWER(CONCAT('%', :title, '%'))) " +
            "AND (:completed IS NULL OR task.completed = :completed) " +
            "AND (:priorityId IS NULL OR task.priority.id = :priorityId) " +
            "AND (:categoryId IS NULL OR task.category.id = :categoryId) " +
            "AND ((CAST(:dateFrom AS timestamp) IS NULL OR task.taskDate >= :dateFrom) " +
            " AND (CAST(:dateTo AS timestamp) IS NULL OR task.taskDate <= :dateTo)) " +
            "AND (task.user.email = :email)"
    )
    Page<Task> findByParams(@Param("title") String title,
                            @Param("completed") Integer completed,
                            @Param("priorityId") Long priorityId,
                            @Param("categoryId") Long categoryId,
                            @Param("email") String email,
                            @Param("dateFrom") Date dateFrom,
                            @Param("dateTo") Date dateTo,
                            Pageable pageable
    );

    List<Task> findByUserEmailOrderByTitleAsc(String email);

}