package com.juniorstart.juniorstart.controller;

import com.juniorstart.juniorstart.model.User;
import com.juniorstart.juniorstart.payload.ChangeMailRequest;
import com.juniorstart.juniorstart.security.CurrentUser;
import com.juniorstart.juniorstart.security.UserPrincipal;
import com.juniorstart.juniorstart.service.UserService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/** Represents an user service.
 * @author Grzegorz Szczęsny
 * @author Dawid Wit
 * @version 1.1
 * @since 1.0
 */
@RestController
public class UserController {
    final private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user/me")
    @PreAuthorize("hasRole('USER')")
    public User getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
        return userService.getCurrentUser(userPrincipal);
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('ADMIN')")
    public Iterable<User> getUsers(
            @RequestParam(required = false) Optional<Long> publicId,
            @RequestParam(required = false) Optional<String> name,
            @RequestParam(required = false) Optional<String> age,
            @RequestParam(required = false) Optional<String> email) {

        return userService.getUsersByProps(publicId, name, age, email);
    }

    @PostMapping("changeMail")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> changeEmail(@RequestBody @Valid ChangeMailRequest changeMail){
        return userService.changeEmail(changeMail);
    }
}
