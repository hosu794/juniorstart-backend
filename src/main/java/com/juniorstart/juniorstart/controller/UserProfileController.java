package com.juniorstart.juniorstart.controller;


import com.juniorstart.juniorstart.model.UserProfile;
import com.juniorstart.juniorstart.service.UserProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/profile")
public class UserProfileController {

    private UserProfileService userProfileService;


    public UserProfileController(UserProfileService userProfileService){
        this.userProfileService = userProfileService;
    }

    @PostMapping("")
    public ResponseEntity<UserProfile> addUserProfile(@RequestBody UserProfile userProfile){
        userProfileService.addUserProfile(userProfile);
        return ResponseEntity.ok(userProfile);

    }


    @GetMapping("/technology_role")
    public ResponseEntity<?>  findMentorByTechnology(@RequestBody List<String> technology, String userRole){
        List<UserProfile> foundMentors =  userProfileService.findByTechnologyAndRole(technology, userRole);
        return ResponseEntity.ok(foundMentors);
    }




    @PutMapping("")
    public ResponseEntity<UserProfile> updateUserProfile(@RequestBody UserProfile userProfile){
        userProfileService.updateUserProfile(userProfile);
        return ResponseEntity.ok(userProfile);
    }







}