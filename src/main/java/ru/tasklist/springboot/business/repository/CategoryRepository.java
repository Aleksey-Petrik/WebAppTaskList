package ru.tasklist.springboot.business.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.tasklist.springboot.business.entity.Category;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("SELECT category FROM Category category WHERE " +
            "(:title IS NULL OR :title = '' " +
            "OR LOWER(category.title) LIKE LOWER(CONCAT('%', :title, '%') ))" +
            "AND category.user.email = :email " +
            "ORDER BY category.title ASC")
    List<Category> findByTitle(@Param("title") String title, @Param("email") String email);

    //Запрос поиска по email по имени метода
    List<Category> findByUserEmailOrderByTitleAsc(String email);

}
