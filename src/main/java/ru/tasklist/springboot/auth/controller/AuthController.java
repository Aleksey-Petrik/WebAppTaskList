package ru.tasklist.springboot.auth.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.tasklist.springboot.auth.entity.Activity;
import ru.tasklist.springboot.auth.entity.User;
import ru.tasklist.springboot.auth.exception.JsonException;
import ru.tasklist.springboot.auth.exception.RoleNotFoundException;
import ru.tasklist.springboot.auth.exception.UserAlreadyActivatedException;
import ru.tasklist.springboot.auth.exception.UserOrEmailAlreadyException;
import ru.tasklist.springboot.auth.service.UserDetailsImpl;
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
    private AuthenticationManager authenticationManager; // стандартный встроенный менеджер Spring, проверяет логин-пароль

    @Autowired
    public AuthController(UserService service, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.service = service;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
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

    // Залогиниться по паролю-пользователю
    // этот метод всем будет доступен для вызова (не будем его "защищать" с помощью токенов, т.к. это не требуется по задаче)
    @PostMapping("/login")
    public ResponseEntity<User> login(@Valid @RequestBody User user) {
        // проверяем логин-пароль
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        // добавляем в Spring-контейнер информацию об авторизации (чтобы Spring понимал, что пользователь успешно вошел и мог использовать его роли и другие параметры)
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // UserDetailsImpl - спец. объект, который хранится в Spring контейнере и содержит данные пользователя
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        // активирован пользователь или нет (проверяем только после того, как пользователь успешно залогиниться)
        if (!userDetails.isActivated()) {
            throw new DisabledException("User disable!");
        }
        // если мы дошло до этой строки, значит пользователь успешно залогинился
        return ResponseEntity.ok().body(userDetails.getUser());
    }


    //Обработчик ошибок, заворачивает в JSON
    @ExceptionHandler(Exception.class)
    public ResponseEntity<JsonException> handlerExceptions(Exception exception) {
        return new ResponseEntity(exception.getMessage()/*new JsonException(exception.getClass().getSimpleName())*/, HttpStatus.BAD_REQUEST);
    }
}
