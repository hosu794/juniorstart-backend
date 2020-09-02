package com.juniorstart.juniorstart.service;

import com.juniorstart.juniorstart.exception.ResourceNotFoundException;
import com.juniorstart.juniorstart.model.User;
import com.juniorstart.juniorstart.payload.ChangeMailRequest;
import com.juniorstart.juniorstart.repository.UserDaoImpl;
import com.juniorstart.juniorstart.security.CurrentUser;
import com.juniorstart.juniorstart.security.UserPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/** Represents an user service.
 * @author Grzegorz Szczęsny
 * @author Dawid Wit
 * @version 1.1
 * @since 1.0
 */
@Service
@Slf4j
public class UserService {
    private final UserDaoImpl userDao;

    public UserService(UserDaoImpl userDao) {
        this.userDao = userDao;
    }

    /** Get a current user credentials.
     * @param userPrincipal The current logged user.
     * @return a current user credentials.
     * @throws ResourceNotFoundException if user not find
     */
    public User getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
        return userDao.findByPublicId(userPrincipal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));
    }

    /** Update user email.
     * @param email new mail for account.
     * @param name name user who change email.
     * @param password for account.
     * @return ResponseEntity<Boolean> is email has changed in repo.
     * @throws ResourceNotFoundException cannot find user by name and password.
     */
    public ResponseEntity<Boolean> changeEmail(String email,String name,String password) {
        ChangeMailRequest changeMail = new ChangeMailRequest(email,name,password);
        User user = userDao.findByNameAndPassword(changeMail.getName(), changeMail.getPassword())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id",changeMail.getName()));
        user.setEmail(changeMail.getEmail());
        user = userDao.save(user);
        return ResponseEntity.ok(changeMail.getEmail().equals(user.getEmail()));
    }
}
