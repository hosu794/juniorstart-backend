package com.juniorstart.juniorstart.service;

import com.juniorstart.juniorstart.exception.ResourceNotFoundException;
import com.juniorstart.juniorstart.model.User;
import com.juniorstart.juniorstart.repository.UserRepository;
import com.juniorstart.juniorstart.security.CurrentUser;
import com.juniorstart.juniorstart.security.UserPrincipal;
import jakarta.validation.constraints.Email;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

/** Represents an user service.
 * @author Grzegorz Szczęsny
 * @author Dawid Wit
 * @version 1.1
 * @since 1.0
 */
@Service
@Slf4j
public class UserService {

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    final private UserRepository userRepository;

    /** Get a current user credentials.
     * @param userPrincipal The current logged user.
     * @return a current user credentials
     * @throws ResourceNotFoundException if user not find
     */
    public User getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
        return userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));
    }

    /** Update user email.
     * @param email new email.
     * @param username login for user
     * @param password password for user
     * @return a current user credentials
     * @throws RuntimeException if email is to long or cannot find user.
     */
    public boolean changeEmail(@Email String email, String username, String password) {
        if (email.length() <= 40) {
            Optional<User> optional = userRepository.findByNameAndPassword(username, password);
            if (optional.isPresent()) {
                User user = optional.get();
                user.setEmail(email);
                user = userRepository.save(user);
                return email.equals(user.getEmail());
            }
            throw new RuntimeException("Cannot find user");
        }
        throw new RuntimeException("To long email");
    }
}
