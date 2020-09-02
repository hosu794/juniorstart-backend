package com.juniorstart.juniorstart.controller;

import com.juniorstart.juniorstart.model.User;

import com.juniorstart.juniorstart.payload.ChangeMailRequest;
import com.juniorstart.juniorstart.security.CurrentUser;
import com.juniorstart.juniorstart.security.UserPrincipal;
import com.juniorstart.juniorstart.service.UserService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/** Represents an user service.
 * @author Grzegorz Szczęsny
 * @author Dawid Wit
 * @version 1.1
 * @since 1.0
 */
@RestController
@RequestMapping("/user")
public class UserController {

    public UserController(UserService userService) {
        this.userService = userService;
    }

    final private UserService userService;

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public User getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
        return userService.getCurrentUser(userPrincipal);
    }

    @PostMapping("/changeMail")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Boolean> changeEmail(@RequestBody ChangeMailRequest changeMailRequest){
        return userService.changeEmail(
                changeMailRequest.getEmail(),
                changeMailRequest.getName(),
                changeMailRequest.getPassword());
    }
}
