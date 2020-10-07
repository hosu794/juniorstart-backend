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

        Optional<UserProfile> foundUser = Optional.ofNullable(userProfileRepository.findByPrivateId(currentUser.getId()).orElseThrow(() ->
                new ResourceNotFoundException("UserProfile", "ID", currentUser.getId())));

        foundUser.get().addUserManyTechnology(userTechnology);

        try {
            userProfileRepository.save(foundUser.get());
            return ResponseEntity.ok(userTechnology);
        }catch(DataIntegrityViolationException exception){
            return ResponseEntity.badRequest().body("Technology already exist");
        }
    }

    public ResponseEntity<?> updateUserTechnology(UserTechnology userTechnology, UserPrincipal currentUser){

        Optional<UserProfile> foundUser = Optional.ofNullable(userProfileRepository.findByPrivateId(currentUser.getId()).orElseThrow(() ->
                new ResourceNotFoundException("UserProfile", "ID", currentUser.getId())));

        foundUser.get().addUserTechnology(userTechnology);
        userProfileRepository.save(foundUser.get());
        return ResponseEntity.ok(userTechnology);

    }

    public ResponseEntity<?> deleteUserTechnology(UserTechnology userTechnology, UserPrincipal currentUser){

        Optional<UserProfile> foundUser = Optional.ofNullable(userProfileRepository.findByPrivateId(currentUser.getId()).orElseThrow(() ->
                new ResourceNotFoundException("UserProfile", "ID", currentUser.getId())));

        if (foundUser.get().getUserTechnology().contains(userTechnology)){
            userTechnologyRepository.deleteById(userTechnology.getId());
            return ResponseEntity.ok(userTechnology);
        }else{
            throw new ResourceNotFoundException("UserTechnology", "ID", userTechnology.getId());
        }
    }
}