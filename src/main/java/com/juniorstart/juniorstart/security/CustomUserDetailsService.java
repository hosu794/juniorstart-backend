package com.juniorstart.juniorstart.security;

import com.juniorstart.juniorstart.exception.ResourceNotFoundException;
import com.juniorstart.juniorstart.model.User;
import com.juniorstart.juniorstart.repository.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Represents a user service.
 * @author Grzegorz Szczęsny
 * @version 1.0
 * @since 1.0
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserDao userDao;

    public CustomUserDetailsService(UserDao userDao) {
        this.userDao = userDao;
    }

    /** Load user by email.
     * @param email The user's email.
     * @return a user's credentials
     * @throws UsernameNotFoundException if user not exists with a given email
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {
        User user = userDao.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with email : " + email)
        );

        return UserPrincipal.create(user);
    }

    /** Load user by identification number
     * @param id The user's identification number.
     * @return a user's credentials
     * @throws ResourceNotFoundException if user not exists with a given identification number
     */
    @Transactional
    public UserDetails loadUserById(Long id) {
        User user = userDao.findByPublicId(id).orElseThrow(
            () -> new ResourceNotFoundException("User", "id", id)
        );

        return UserPrincipal.create(user);
    }
}