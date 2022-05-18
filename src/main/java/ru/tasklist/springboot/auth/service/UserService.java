package ru.tasklist.springboot.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.tasklist.springboot.auth.entity.Activity;
import ru.tasklist.springboot.auth.entity.Role;
import ru.tasklist.springboot.auth.entity.User;
import ru.tasklist.springboot.auth.repository.ActivityRepository;
import ru.tasklist.springboot.auth.repository.RoleRepository;
import ru.tasklist.springboot.auth.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
/*
 @Transactional будет применяться к каждому методу сервиса.
 Пригодится на будущее, если в одном методе будет несколько вызовов в БД - все будут выполняться в одной транзакции.
 Можно будет настраивать транзакции точечно по необходимости.
 Если в методе при вызове репозитория возникнет исключение - все выполненные вызовы к БД из данного метода откатятся (Rollback)
*/
@Transactional
public class UserService {
    public static final String DEFAULT_ROLE = "USER";// такая роль должна быть обязательно в таблице БД

    private final UserRepository userRepository;// работа с пользователями
    private final RoleRepository roleRepository;// работа с ролями
    private final ActivityRepository activityRepository;// работа с активностями

    @Autowired
    public UserService(UserRepository repository, RoleRepository roleRepository, ActivityRepository activityRepository) {
        this.userRepository = repository;
        this.roleRepository = roleRepository;
        this.activityRepository = activityRepository;
    }

    public Optional<User> findById(Long userId) {
        return userRepository.findById(userId);
    }

    //Регистрация нового пользователя, предварительно проверив наличие его в базе
    public boolean register(User user, Activity activity) {
        // сохраняем данные в БД - если будет ошибка - никакие данные в БД не попадут, произойдет Rollback (откат транзакции) - благодаря @Transactional
        return userRepository.save(user) != null
                && activityRepository.save(activity) != null;
    }

    // проверка, существует ли пользователь в БД (email и username должны быть уникальными в таблице)
    public boolean userExistByUsername(String username) {
        return userRepository.existByUsername(username);
    }

    // проверка, существует ли пользователь в БД (email и username должны быть уникальными в таблице)
    public boolean userExistByEmail(String email) {
        return userRepository.existByEmail(email);
    }

    public Optional<Role> findRoleByName(String nameRole) {
        return roleRepository.findByName(nameRole);
    }

    public Optional<Activity> findActivityByUuid(String uuid) {
        return activityRepository.findByUuid(uuid);
    }

    // true с конвертируется в 1, т.к. указали @Type(type = "org.hibernate.type.NumericBooleanType") в классе Activity
    public int activate(String uuid) {
        return activityRepository.changeActivated(true, uuid);
    }

    // false с конвертируется в 0, т.к. указали @Type(type = "org.hibernate.type.NumericBooleanType") в классе Activity
    public int deactivate(String uuid) {
        return activityRepository.changeActivated(false, uuid);
    }

    public int updatePassword(String password, String username) {
        return userRepository.updatePassword(password, username);
    }
}
