package com.juniorstart.juniorstart.controller;

import com.juniorstart.juniorstart.model.UserTechnology;
import com.juniorstart.juniorstart.security.CurrentUser;
import com.juniorstart.juniorstart.security.UserPrincipal;
import com.juniorstart.juniorstart.service.UserTechnologyService;
import com.juniorstart.juniorstart.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Set;

@RestController
@RequestMapping("/user/technology")
public class UserTechnologyController {

    private UserTechnologyService userTechnologyService;

    final private UserService userService;

    public UserTechnologyController(UserTechnologyService userTechnologyService,
                                    UserService userService){
        this.userTechnologyService = userTechnologyService;
        this.userService = userService;
    }

    @PostMapping("")
    public ResponseEntity<?> addUserProfileTechnology(@RequestBody Set<UserTechnology> userTechnology, @CurrentUser UserPrincipal userPrincipal){
        return userTechnologyService.addUserTechnology(userTechnology, userPrincipal);
    }

    @PutMapping("")
    public ResponseEntity<?> updateUserProfileTechnology(@RequestBody UserTechnology userTechnology, @CurrentUser UserPrincipal userPrincipal){
        return userTechnologyService.updateUserTechnology(userTechnology, userPrincipal);
    }

    @DeleteMapping("")
    public ResponseEntity<?> deleteUserProfileTechnology(@RequestBody UserTechnology userTechnology, @CurrentUser UserPrincipal userPrincipal){
        return userTechnologyService.deleteUserTechnology(userTechnology, userPrincipal);
    }
}