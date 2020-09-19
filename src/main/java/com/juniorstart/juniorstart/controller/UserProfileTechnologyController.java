package com.juniorstart.juniorstart.controller;


import com.juniorstart.juniorstart.helpers.getLogedUserId;
import com.juniorstart.juniorstart.model.UserTechnology;
import com.juniorstart.juniorstart.service.UserProfileTechnologyService;
import com.juniorstart.juniorstart.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user/technology")
public class UserProfileTechnologyController {

    private UserProfileTechnologyService userProfileTechnologyService;

    final private UserService userService;


    public UserProfileTechnologyController(UserProfileTechnologyService userProfileTechnologyService,
                                           UserService userService){
        this.userProfileTechnologyService = userProfileTechnologyService;
        this.userService = userService;
    }

    @PostMapping("")
    public ResponseEntity<?> addUserProfileTechnology(@RequestBody List<UserTechnology> userTechnology){

        return ResponseEntity.ok("cos");

    }

    @PutMapping("")
    public ResponseEntity<?> updateUserProfileTechnology(@RequestBody UserTechnology userTechnology){
        //userProfileTechnologyService.
        return ResponseEntity.ok("cos");

    }


    @DeleteMapping("")
    public ResponseEntity<?> deleteUserProfileTechnology(@RequestBody UserTechnology userTechnology){

        return ResponseEntity.ok("cos");
    }

    @GetMapping("")
    public ResponseEntity<?> test_temp(){

        UUID userId = new getLogedUserId().userId;
        System.out.println(userId);

            return ResponseEntity.ok("cos111");
    }



}
/*      userRepository.save(user);

        UserTechnology userTechnology = new UserTechnology();
        userTechnology.setTechnologyName("Java");

        UserProfile userProfile = new UserProfile();
        userProfile.setUser(user);
        userProfile.setUserRole(ListUserRole.MENTOR);
        userProfile.addUserTechnology(userTechnology);
        userProfileRepository.save(userProfile);

 */