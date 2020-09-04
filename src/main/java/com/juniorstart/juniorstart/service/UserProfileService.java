package com.juniorstart.juniorstart.service;


import com.juniorstart.juniorstart.helpers.getLogedUserId;
import com.juniorstart.juniorstart.model.User;
import com.juniorstart.juniorstart.model.UserProfile;
import com.juniorstart.juniorstart.model.UserTechnology;
import com.juniorstart.juniorstart.repository.UserProfileRepository;

import com.juniorstart.juniorstart.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@Slf4j
public class UserProfileService {

    @Autowired
    UserProfileRepository userProfileRepository;

    @Autowired
    UserRepository userRepository;

  //  Long userId = new getLogedUserId().userID;


    UserProfileService(UserProfileRepository userProfileRepository){
        this.userProfileRepository=userProfileRepository;
    }


    public UserProfile addUserProfile(UserProfile userProfile){

        //Long userId = new getLogedUserId().userID;
        //Long tmp = userId;


        Long userId= 5L;

        Optional<User> foundUser = userRepository.findById(userId);

        if(foundUser.isPresent()){
            userProfile.setUser(foundUser.get());
            userProfile.setId(foundUser.get().getId());
            userProfileRepository.save(userProfile);
        }

        return userProfile;
    }


    public UserProfile updateUserProfile(UserProfile userProfile){

        Long userId= 5L;


        Optional<UserProfile> foundUser = userProfileRepository.findById(userId);

        if(foundUser.isPresent()){
            userProfileRepository.save(userProfile);
        }

        return userProfile;

    }


    public List<UserProfile> findMentorByTechnology(List<String> technology){

        List<UserProfile> foundMentorsByTechnology = userProfileRepository.findAllByUserTechnologyIn(technology);

       System.out.println(foundMentorsByTechnology);
        return foundMentorsByTechnology;

    }

    public List<UserProfile> findOneMentorByTechnology(String technology){

        List<UserProfile> foundMentorsByTechnology = userProfileRepository.findAllByUserTechnology(technology);

        System.out.println(foundMentorsByTechnology);
        return foundMentorsByTechnology;

    }





}
