package ru.tasklist.springboot.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.tasklist.springboot.auth.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // для проверки существования email или username возвращаем только true или false (не возвращаем весь объект User, нет смысла)
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN TRUE ELSE FALSE END FROM User u WHERE LOWER(u.email) = LOWER(:email) ")
    boolean existByEmail(@Param("email") String email);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN TRUE ELSE FALSE END FROM User u WHERE LOWER(u.username) = LOWER(:username)")
    boolean existByUsername(@Param("username") String username);

    // используем обертку Optional - контейнер, который хранит значение или null - позволяет избежать ошибки NullPointerException
    Optional<User> findByUsername(String username);// поиск по username

    // используем обертку Optional - контейнер, который хранит значение или null - позволяет избежать ошибки NullPointerException
    Optional<User> findByEmail(String email);// поиск по email

    // обновление пароля
    @Modifying // если запрос изменяет данные - желательно добавлять эту аннотацию
    @Transactional // если запрос изменяет данные - желательно добавлять эту аннотацию
    @Query("UPDATE User u SET u.password = :password WHERE u.username=:username")
    // обновление записи с нужным UUID
    int updatePassword(@Param("password") String password, @Param("username") String username); // возвращает int (сколько записей обновил) - в данном случае всегда должен возвращать 1
}
