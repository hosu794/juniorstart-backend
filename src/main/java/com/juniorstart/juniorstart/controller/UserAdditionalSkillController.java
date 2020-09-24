package com.juniorstart.juniorstart.controller;

import com.juniorstart.juniorstart.model.UserAdditionalSkill;
import com.juniorstart.juniorstart.model.UserTechnology;
import com.juniorstart.juniorstart.security.CurrentUser;
import com.juniorstart.juniorstart.security.UserPrincipal;
import com.juniorstart.juniorstart.service.UserAdditionalSkillService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
public class UserAdditionalSkillController {

    final private UserAdditionalSkillService userAdditionalSkillService;

    public UserAdditionalSkillController(UserAdditionalSkillService userAdditionalSkillService) {
        this.userAdditionalSkillService = userAdditionalSkillService;
    }

    @PostMapping()
    public ResponseEntity<?> addUserAdditionalSKill(@RequestBody Set<UserAdditionalSkill> userAdditionalSkill, UserTechnology userTechnology, @CurrentUser UserPrincipal userPrincipal) {
        return userAdditionalSkillService.addUserAdditionalSKill(userAdditionalSkill, userTechnology ,userPrincipal);
    }

    @PutMapping()
    public ResponseEntity<?> changeUserAdditionalSkill(@RequestBody UserAdditionalSkill userAdditionalSkill, UserTechnology userTechnology,@CurrentUser UserPrincipal userPrincipal) {
        return userAdditionalSkillService.updateUserAdditionalSkill(userAdditionalSkill, userTechnology ,userPrincipal);
    }

    @DeleteMapping()
    public ResponseEntity<?> deleteUserAdditionalSkill(@RequestBody UserAdditionalSkill userAdditionalSkill, UserTechnology userTechnology, @CurrentUser UserPrincipal userPrincipal) {
        return userAdditionalSkillService.deleteUserAdditionalSkill(userAdditionalSkill, userTechnology ,userPrincipal);
    }
}