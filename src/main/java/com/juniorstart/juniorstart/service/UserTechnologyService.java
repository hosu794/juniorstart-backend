package com.juniorstart.juniorstart.service;

import com.juniorstart.juniorstart.exception.ResourceNotFoundException;
import com.juniorstart.juniorstart.model.UserProfile;
import com.juniorstart.juniorstart.model.UserTechnology;
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
public class UserTechnologyService {

    final private UserProfileRepository userProfileRepository;

    final private UserTechnologyRepository userTechnologyRepository;

    public UserTechnologyService(UserProfileRepository userProfileRepository, UserTechnologyRepository userTechnologyRepository){
        this.userProfileRepository = userProfileRepository;
        this.userTechnologyRepository = userTechnologyRepository;
    }

    public ResponseEntity<?> addUserTechnology(Set<UserTechnology> userTechnology, UserPrincipal currentUser){

        Optional<UserProfile> foundUser = findUser(currentUser.getId());

        foundUser.get().addUserManyTechnology(userTechnology);

        try {
            return ResponseEntity.ok(userProfileRepository.save(foundUser.get()));
        } catch(DataIntegrityViolationException exception) {
            return ResponseEntity.badRequest().body("Technology already exist");
        }
    }

    public ResponseEntity<?> updateUserTechnology(UserTechnology userTechnology, UserPrincipal currentUser){

        Optional<UserProfile> foundUser = findUser(currentUser.getId());

        foundUser.get().addUserTechnology(userTechnology);
        return ResponseEntity.ok(userProfileRepository.save(foundUser.get()));
    }

    public ResponseEntity<?> deleteUserTechnology(UserTechnology userTechnology, UserPrincipal currentUser){

        Optional<UserProfile> foundUser = findUser(currentUser.getId());

        if (foundUser.get().getUserTechnology().contains(userTechnology)){
            userTechnologyRepository.deleteById(userTechnology.getId());
            return ResponseEntity.ok(userTechnology);
        } else {
            throw new ResourceNotFoundException("UserTechnology", "ID", userTechnology.getId());
        }
    }

    public Optional<UserProfile> findUser(UUID id) {
        return Optional.ofNullable(userProfileRepository.findByPrivateId(id).orElseThrow(() ->
                  new ResourceNotFoundException("UserProfile", "ID", id)));
    }
}