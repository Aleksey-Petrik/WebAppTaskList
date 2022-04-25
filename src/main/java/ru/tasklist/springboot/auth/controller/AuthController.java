package ru.tasklist.springboot.auth.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.tasklist.springboot.auth.entity.Activity;
import ru.tasklist.springboot.auth.entity.User;
import ru.tasklist.springboot.auth.exception.JsonException;
import ru.tasklist.springboot.auth.exception.RoleNotFoundException;
import ru.tasklist.springboot.auth.exception.UserAlreadyActivatedException;
import ru.tasklist.springboot.auth.exception.UserOrEmailAlreadyException;
import ru.tasklist.springboot.auth.service.UserService;

import javax.validation.Valid;
import java.util.UUID;

import static ru.tasklist.springboot.auth.service.UserService.DEFAULT_ROLE;

@Log4j2
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService service;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthController(UserService service, PasswordEncoder passwordEncoder) {
        this.service = service;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/id")
    public ResponseEntity<User> findById(@RequestBody Long userId) {
        log.info("Get user by id - {}", userId);
        return ResponseEntity.ok(service.findById(userId).get());
    }

    @PutMapping("/register")
    public ResponseEntity register(@Valid @RequestBody User user) {
        log.info("Register new user {}", user);

        if (service.userExistByEmail(user.getEmail())
                || service.userExistByUsername(user.getUsername())) {
            throw new UserOrEmailAlreadyException("Username or email already exists!");
        }
        //Добавляем роль по умолчанию для нового пользователя
        user.addRole(service.findRoleByName(DEFAULT_ROLE)
                .orElseThrow(() -> new RoleNotFoundException("Default role not found!!!")));

        //Кодируем пароль алгоритмом BCrypt
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        //Запись активности пользователя
        Activity activity = new Activity();
        activity.setUuid(UUID.randomUUID().toString());
        activity.setUser(user);

        return service.register(user, activity)
                ? ResponseEntity.ok().build()
                : new ResponseEntity("Error in registers.", HttpStatus.NOT_ACCEPTABLE);
    }

    @PostMapping("/activate-account")
    public ResponseEntity<Boolean> activateUser(@RequestBody String uuid) {
        //Проверка на существование пользователя
        Activity activity = service.findActivityByUuid(uuid)
                .orElseThrow(() -> new UsernameNotFoundException("Activity for user not found bad uuid - " + uuid));

        //Проверка на ранее произведенную активацию
        if (activity.isActivated()) {
            throw new UserAlreadyActivatedException("User already activated!");
        }
        //Количество активированных записей, в теории всегда один
        int updateCount = service.activate(activity.getUuid());

        return ResponseEntity.ok(updateCount == 1);
    }

    //Обработчик ошибок, заворачивает в JSON
    @ExceptionHandler(Exception.class)
    public ResponseEntity<JsonException> handlerExceptions(Exception exception) {
        return new ResponseEntity(exception.getMessage()/*new JsonException(exception.getClass().getSimpleName())*/, HttpStatus.BAD_REQUEST);
    }
}
