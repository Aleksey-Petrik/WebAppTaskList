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
@Transactional
public class UserService {
    public static final String DEFAULT_ROLE = "USER";

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ActivityRepository activityRepository;

    @Autowired
    public UserService(UserRepository repository, RoleRepository roleRepository, ActivityRepository activityRepository) {
        this.userRepository = repository;
        this.roleRepository = roleRepository;
        this.activityRepository = activityRepository;
    }

    public Optional<User> findById(Long userId) {
        return userRepository.findById(userId);
    }

    public boolean register(User user, Activity activity) {
        return userRepository.save(user) != null
                && activityRepository.save(activity) != null;
    }

    public boolean userExistByUsername(String username) {
        return userRepository.existByUsername(username);
    }

    public boolean userExistByEmail(String email) {
        return userRepository.existByEmail(email);
    }

    public Optional<Role> findRoleByName(String nameRole) {
        return roleRepository.findByName(nameRole);
    }

}
