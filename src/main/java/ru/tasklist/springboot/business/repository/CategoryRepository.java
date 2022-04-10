package ru.tasklist.springboot.business.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.tasklist.springboot.business.entity.Category;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    //Запрос поиска по email по имени метода
    List<Category> findByUserEmailOrderByTitleAsc(String email);

}
