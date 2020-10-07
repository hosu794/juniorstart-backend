package com.juniorstart.juniorstart.controller;

import com.juniorstart.juniorstart.model.UserAdditionalSkill;
import com.juniorstart.juniorstart.model.UserTechnology;
import com.juniorstart.juniorstart.security.CurrentUser;
import com.juniorstart.juniorstart.security.UserPrincipal;
import com.juniorstart.juniorstart.service.UserAdditionalSkillService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/user/additional_skill")
public class UserAdditionalSkillController {

    private final UserAdditionalSkillService userAdditionalSkillService;

    public UserAdditionalSkillController(UserAdditionalSkillService userAdditionalSkillService) {
        this.userAdditionalSkillService = userAdditionalSkillService;
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping()
    public ResponseEntity<?> addUserAdditionalSKill(@RequestBody Set<UserAdditionalSkill> userAdditionalSkill, UserTechnology userTechnology, @CurrentUser UserPrincipal userPrincipal) {
        return userAdditionalSkillService.addUserAdditionalSKill(userAdditionalSkill, userTechnology ,userPrincipal);
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping()
    public ResponseEntity<?> changeUserAdditionalSkill(@RequestBody UserAdditionalSkill userAdditionalSkill, UserTechnology userTechnology,@CurrentUser UserPrincipal userPrincipal) {
        return userAdditionalSkillService.updateUserAdditionalSkill(userAdditionalSkill, userTechnology ,userPrincipal);
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping()
    public ResponseEntity<?> deleteUserAdditionalSkill(@RequestBody UserAdditionalSkill userAdditionalSkill, UserTechnology userTechnology, @CurrentUser UserPrincipal userPrincipal) {
        return userAdditionalSkillService.deleteUserAdditionalSkill(userAdditionalSkill, userTechnology ,userPrincipal);
    }
}