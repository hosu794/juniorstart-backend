package com.juniorstart.juniorstart.service;

import com.juniorstart.juniorstart.exception.ResourceNotFoundException;
import com.juniorstart.juniorstart.model.UserAdditionalSkill;
import com.juniorstart.juniorstart.model.UserTechnology;
import com.juniorstart.juniorstart.repository.UserAdditionalSkillRepository;
import com.juniorstart.juniorstart.repository.UserProfileRepository;
import com.juniorstart.juniorstart.repository.UserTechnologyRepository;
import com.juniorstart.juniorstart.security.UserPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@Slf4j
public class UserAdditionalSkillService {

    final private UserTechnologyRepository userTechnologyRepository;
    final private UserProfileRepository userProfileRepository;
    final private UserAdditionalSkillRepository userAdditionalSkillRepository;

    UserAdditionalSkillService(UserTechnologyRepository userTechnologyRepository, UserProfileRepository userProfileRepository,
                               UserAdditionalSkillRepository userAdditionalSkillRepository) {
        this.userTechnologyRepository = userTechnologyRepository;
        this.userProfileRepository = userProfileRepository;
        this.userAdditionalSkillRepository = userAdditionalSkillRepository;
    }

    public ResponseEntity<?> addUserAdditionalSKill(Set<UserAdditionalSkill> userAdditionalSkill, UserTechnology userTechnology , UserPrincipal currentUser) {

        validateParametrs(userTechnology, currentUser.getId());
        userTechnology.addManyAdditionalSkill(userAdditionalSkill);

        try {
            userTechnologyRepository.save(userTechnology);
            return  ResponseEntity.ok(userAdditionalSkill);
        } catch (DataIntegrityViolationException exception){
            return ResponseEntity.badRequest().body("Technology already exist");
        }
    }

    public ResponseEntity<?> updateUserAdditionalSkill(UserAdditionalSkill userAdditionalSkill, UserTechnology userTechnology, UserPrincipal currentUser) {

        validateParametrs(userTechnology, currentUser.getId());

        userTechnology.addAdditionalSKill(userAdditionalSkill);
        userTechnologyRepository.save(userTechnology);

        return ResponseEntity.ok(userAdditionalSkill);
    }

    public ResponseEntity<?> deleteUserAdditionalSkill(UserAdditionalSkill userAdditionalSkill, UserTechnology userTechnology, UserPrincipal currentUser) {

        validateParametrs(userTechnology, currentUser.getId());

        if(userTechnology.getUserAdditionalSkills().contains(userAdditionalSkill)){
            userAdditionalSkillRepository.deleteById(userAdditionalSkill.getId());
            return ResponseEntity.ok(userAdditionalSkill);
        }else{
            throw new ResourceNotFoundException("UserAdditionalSkill", "ID", userAdditionalSkill.getId());
        }
    }

    public void validateParametrs(UserTechnology userTechnology, UUID userId){
        userProfileRepository.findByPrivateId(userId).orElseThrow(() ->
                new ResourceNotFoundException("UserProfile", "ID", userId));
        userTechnologyRepository.findById(userTechnology.getId()).orElseThrow(() ->
                new ResourceNotFoundException("UserTechnology", "ID", userTechnology.getId()));
    }
}