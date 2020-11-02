package com.juniorstart.juniorstart.controller;

import com.juniorstart.juniorstart.model.UserProfile;
import com.juniorstart.juniorstart.payload.PagedResponse;
import com.juniorstart.juniorstart.payload.UserRoleOrTechnologyRequest;
import com.juniorstart.juniorstart.security.CurrentUser;
import com.juniorstart.juniorstart.security.UserPrincipal;
import com.juniorstart.juniorstart.service.UserProfileAvatarService;
import com.juniorstart.juniorstart.service.UserProfileService;
import com.juniorstart.juniorstart.util.AppConstants;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/user/profile")
public class UserProfileController {

    final private UserProfileService userProfileService;
    final private UserProfileAvatarService userProfileAvatarService;

    public UserProfileController(UserProfileService userProfileService, UserProfileAvatarService userProfileAvatarService) {
        this.userProfileService = userProfileService;
        this.userProfileAvatarService = userProfileAvatarService;
    }

    @GetMapping("/technology_role")
    public PagedResponse<UserProfile.UserProfileDto> findByUserRolesAndTechnologies(@RequestBody UserRoleOrTechnologyRequest userRoleOrTechnologyRequest,
                                                                                    @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                                                    @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {

        return userProfileService.selectionForSearching(userRoleOrTechnologyRequest, page, size);
    }

    @GetMapping("/avatar")
    public ResponseEntity<?> getUserAvatar(@CurrentUser UserPrincipal userPrincipal) {

        return userProfileAvatarService.getUserAvatar(userPrincipal);
    }

    @PostMapping("/avatar")
    public ResponseEntity<?> addUserAvatar(@RequestParam("imageFile") MultipartFile file, @CurrentUser UserPrincipal userPrincipal) throws IOException {

        return userProfileAvatarService.addUserAvatar(file,userPrincipal);
    }

    @PutMapping("/avatar")
    public ResponseEntity<?> updateUserAvatar(@RequestParam("imageFile") MultipartFile file, @CurrentUser UserPrincipal userPrincipal) throws IOException {

        return userProfileAvatarService.updateUserAvatar(file,userPrincipal);
    }

    @DeleteMapping("/avatar")
    public ResponseEntity<?> deleteUserAvatar(@CurrentUser UserPrincipal userPrincipal) {

        return userProfileAvatarService.deleteUserAvatar(userPrincipal);
    }

}