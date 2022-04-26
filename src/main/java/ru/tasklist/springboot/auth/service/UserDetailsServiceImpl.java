package ru.tasklist.springboot.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.tasklist.springboot.auth.entity.User;
import ru.tasklist.springboot.auth.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> userOptional = userRepository.findByUsername(username);

        if (!userOptional.isPresent()) {
            userOptional = userRepository.findByEmail(username);
            if (!userOptional.isPresent()) {
                throw new UsernameNotFoundException("Username not found with username or email " + username);
            }
        }

        return new UserDetailsImpl(userOptional.get());
    }

    public UserDetails loadUserById(long userId) throws UsernameNotFoundException {

        Optional<User> userOptional = userRepository.findById(userId);

        if (!userOptional.isPresent()) {
            throw new UsernameNotFoundException("Username not found with id " + userId);
        }

        return new UserDetailsImpl(userOptional.get());
    }

}
