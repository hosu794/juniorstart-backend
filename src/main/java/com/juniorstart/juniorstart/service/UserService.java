package com.juniorstart.juniorstart.service;

import com.juniorstart.juniorstart.exception.ResourceNotFoundException;
import com.juniorstart.juniorstart.model.User;
import com.juniorstart.juniorstart.repository.UserRepository;
import com.juniorstart.juniorstart.security.CurrentUser;
import com.juniorstart.juniorstart.security.UserPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/** Represents an user service.
 * @author Grzegorz Szczęsny
 * @version 1.0
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

}
