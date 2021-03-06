package com.juniorstart.juniorstart.controller;

import com.juniorstart.juniorstart.model.User;
import com.juniorstart.juniorstart.model.UserProfile;
import com.juniorstart.juniorstart.payload.ChangeMailRequest;
import com.juniorstart.juniorstart.payload.ChangePasswordRequest;
import com.juniorstart.juniorstart.payload.ChangeStatusRequest;
import com.juniorstart.juniorstart.payload.UserSummary;
import com.juniorstart.juniorstart.security.CurrentUser;
import com.juniorstart.juniorstart.security.UserPrincipal;
import com.juniorstart.juniorstart.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;

/** Represents an user service.
 * @author Grzegorz Szczęsny
 * @author Dawid Wit
 * @version 1.2
 * @since 1.1
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    final private UserService userService;

    @GetMapping("/user/me")
    @PreAuthorize("hasRole('USER')")
    public UserSummary getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
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

    @PostMapping("changePassword")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> changePassword(@RequestBody @Valid ChangePasswordRequest changePassword){
        return userService.changePassword(changePassword);
    }

    @PostMapping("changeStatus")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> changeStatus(@RequestBody @Valid ChangeStatusRequest request){
        return userService.changeStatus(request);
    }

    @GetMapping("statusList")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getStatusList(){
        return userService.getStatusList();
    }

    @GetMapping("/users/summaries")
    @PreAuthorize("hasRole('USER')")
    public List<UserSummary> getAllUserSummaries(@CurrentUser UserPrincipal userPrincipal) {
        return userService.findAllUserSummaries(userPrincipal);
    }

}
