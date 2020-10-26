package com.juniorstart.juniorstart.controller;

import com.juniorstart.juniorstart.model.UserProfile;
import com.juniorstart.juniorstart.payload.PagedResponse;
import com.juniorstart.juniorstart.payload.UserRoleOrTechnologyRequest;
import com.juniorstart.juniorstart.service.UserProfileService;
import com.juniorstart.juniorstart.util.AppConstants;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/profile")
public class UserProfileController {

    final private UserProfileService userProfileService;

    public UserProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @GetMapping("/technology_role")
    public PagedResponse<UserProfile.UserProfileDto> findByUserRolesAndTechnologies(@RequestBody UserRoleOrTechnologyRequest userRoleOrTechnologyRequest,
                                                                                    @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                                                    @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {

        return userProfileService.selectionForSearching(userRoleOrTechnologyRequest, page, size);
    }
}