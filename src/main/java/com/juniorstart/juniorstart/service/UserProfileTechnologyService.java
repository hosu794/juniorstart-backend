package com.juniorstart.juniorstart.service;


import com.juniorstart.juniorstart.helpers.getLogedUserId;
import com.juniorstart.juniorstart.model.UserProfile;
import com.juniorstart.juniorstart.model.UserRole;
import com.juniorstart.juniorstart.model.UserTechnology;
import com.juniorstart.juniorstart.repository.UserProfileRepository;
import com.juniorstart.juniorstart.repository.UserProfileTechnologyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class UserProfileTechnologyService {


   // @Autowired
   // UserProfileTechnologyRepository userProfileTechnologyRepository;

    final private UserProfileRepository userProfileRepository;

    public UserProfileTechnologyService(UserProfileRepository userProfileRepository){
        this.userProfileRepository = userProfileRepository;
    }


    //UUID userId = new getLogedUserId().userId;


    /*
    public UserProfile addUserProfileTechnology(UserTechnology userTechnology){
        Optional<UserProfile> foundUser = userProfileRepository.findByPrivateId(userId);
        if (foundUser.isPresent()){
            foundUser.get().setUserTechnology(userTechnology);
        }
        userTechnology.setUsersProfile(foundUser.get());
        userProfileTechnologyRepository.save(userTechnology);

    }

    public UserProfile updateUserProfileTag(UserTechnology userTechnology){

    }

    public UserProfile deleteUserProfileTag(UserTechnology userTechnology){

    }
     */


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