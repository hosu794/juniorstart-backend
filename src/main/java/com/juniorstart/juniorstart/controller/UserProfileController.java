package com.juniorstart.juniorstart.controller;

import com.juniorstart.juniorstart.payload.UserRoleOrTechnologyRequest;
import com.juniorstart.juniorstart.service.UserProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/profile")
public class UserProfileController {

    final private UserProfileService userProfileService;

    public UserProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @GetMapping("/technology_role")
    public ResponseEntity<?>  findMentorByTechnology(@RequestBody UserRoleOrTechnologyRequest userRoleOrTechnologyRequest) {
        return ResponseEntity.ok(userProfileService.selectionForSearching(userRoleOrTechnologyRequest));
    }
}
