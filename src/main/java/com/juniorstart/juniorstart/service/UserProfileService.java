package com.juniorstart.juniorstart.service;

import com.juniorstart.juniorstart.exception.BadRequestException;
import com.juniorstart.juniorstart.exception.ResourceNotFoundException;
import com.juniorstart.juniorstart.model.ListUserRole;
import com.juniorstart.juniorstart.model.User;
import com.juniorstart.juniorstart.model.UserProfile;
import com.juniorstart.juniorstart.payload.interfaces.InterfaceChangeRequest;
import com.juniorstart.juniorstart.repository.UserDao;
import com.juniorstart.juniorstart.repository.UserProfileRepository;

import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.implementation.bytecode.Throw;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.text.WordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/** Represents an userProfile service.
 * @author Rafał maduzia
 * @version 1.0
 */
@Service
@Slf4j
public class UserProfileService {

    @Autowired
    UserProfileRepository userProfileRepository;

    @Autowired
    UserDao userRepository;

    //Ignore This, Future for Next task
  //  Long userId = new getLogedUserId().userID;

    UserProfileService(UserProfileRepository userProfileRepository) {
        this.userProfileRepository=userProfileRepository;
    }

    //TODO GONNA WORK ON ADD USER PROFILE ON NEXT TASK
    public UserProfile addUserProfile(UserProfile userProfile) {

        //Long userId = new getLogedUserId().userID;
        //Long tmp = userId;
        Long userId= 5L;

        Optional<User> foundUser = userRepository.findByPublicId(userId);

        if(foundUser.isPresent()){
            userProfile.setUser(foundUser.get());
            userProfile.setPrivateId(foundUser.get().getPrivateId());
            userProfileRepository.save(userProfile);
        }

        return userProfile;
    }

    //TODO GONNA WORK ON UPDATE USER PROFILE ON NEXT TASK
    public UserProfile updateUserProfile(UserProfile userProfile) {

        Long userId= 5L;

        Optional<UserProfile> foundUser = userProfileRepository.findById(userId);

        if(foundUser.isPresent()){
            userProfileRepository.save(userProfile);
        }
        return userProfile;
    }

    /** Get a List of UserProfile.
     * @param technology Technology name you are looking for
     * @param userRole UserRole(JUNIOR,MENTOR etc) name you are looking for
     * @return list of UserProfile
     * @throws ResourceNotFoundException if userRole isn't valid
     */
    public List<UserProfile> findByTechnologyAndRole(List<String> technology, List<String> userRole) {
        List<ListUserRole> convertedUserRole= validateAndReturnAsEnum(userRole);
        List<String> convertedTechnology = technology.stream().map(WordUtils::capitalize).collect(Collectors.toList());
        return userProfileRepository.findByUserTechnology_technologyNameInAndUserRoleIn(convertedTechnology, convertedUserRole);
    }

    /** Get a List of UserProfile.
     * @param technology Technology name you are looking for
     * @return list of UserProfile
     */
    public List<UserProfile> findByTechnology(List<String> technology) {
        List<String> convertedTechnology = technology.stream().map(WordUtils::capitalize).collect(Collectors.toList());
        return userProfileRepository.findByUserTechnology_technologyNameIn(convertedTechnology);
    }

    /** Get a List of UserProfile.
     * @param userRole UserRole(JUNIOR,MENTOR etc) name you are looking for
     * @return list of UserProfile
     * @throws ResourceNotFoundException if userRole isn't valid
     */
    public List<UserProfile> findByUserRole(List<String> userRole) {
        List<ListUserRole> convertedUserRole= validateAndReturnAsEnum(userRole);
        return userProfileRepository.findByUserRoleIn(convertedUserRole);

    }

    /** Validate Listof String and return ENUM values of ListUserRole.
     * @param userRole UserRole(JUNIOR,MENTOR etc) name you are looking for
     * @return list of UserProfile
     * @throws ResourceNotFoundException if userRole isn't valid
     */
    public List<ListUserRole> validateAndReturnAsEnum(List<String> userRole) {
        for (String value : userRole) {
            if (!EnumUtils.isValidEnumIgnoreCase(ListUserRole.class, value)) {
                throw new BadRequestException("Pick value from List");
            }
        }
        return userRole.stream()
                .map(String::toUpperCase)
                .map(ListUserRole::valueOf).collect(Collectors.toList());
    }

}
