package com.juniorstart.juniorstart.service;

import com.juniorstart.juniorstart.exception.BadRequestException;
import com.juniorstart.juniorstart.exception.ResourceNotFoundException;
import com.juniorstart.juniorstart.model.UserRole;
import com.juniorstart.juniorstart.model.UserProfile;
import com.juniorstart.juniorstart.payload.GetUserRoleOrTechnologyRequest;
import com.juniorstart.juniorstart.repository.UserProfileRepository;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.text.WordUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/** Represents an userProfile service.
 * @author Rafał maduzia
 * @version 1.0
 */
@Service
@Slf4j
public class UserProfileService {

    final private UserProfileRepository userProfileRepository;

    public UserProfileService(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    /** Main function to Get a List of UserProfile. Checks which method to choose
     * @param getUserRoleOrTechnologyRequest Technology or UserRole you are looking for
     * @return list of UserProfile
     */
    public List<UserProfile> selectionForSearching(GetUserRoleOrTechnologyRequest getUserRoleOrTechnologyRequest){
        List<String> technology = getUserRoleOrTechnologyRequest.getTechnology();
        List<String> userRole = getUserRoleOrTechnologyRequest.getUserRole();

        if (!technology.isEmpty() && !userRole.isEmpty()){
            List<UserRole> convertedUserRole= validateAndReturnAsEnum(userRole);
            return findByTechnologyAndRole(technology, convertedUserRole);
        }
        if (!technology.isEmpty()){
            return findByTechnology(technology);
        }else{
            List<UserRole> convertedUserRole= validateAndReturnAsEnum(userRole);
            return findByUserRole(convertedUserRole);
        }
    }

    /** Get a List of UserProfile.
     * @param technology Technology name you are looking for
     * @param userRole UserRole(JUNIOR,MENTOR etc) name you are looking for
     * @return list of UserProfile
     * @throws ResourceNotFoundException if userRole isn't valid
     */
    public List<UserProfile> findByTechnologyAndRole(List<String> technology, List<UserRole> userRole) {
        List<String> convertedTechnology = technology.stream().map(WordUtils::capitalize).collect(Collectors.toList());
        return userProfileRepository.findByUserTechnology_technologyNameInAndUserRoleIn(convertedTechnology, userRole);
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
    public List<UserProfile> findByUserRole(List<UserRole> userRole) {
        return userProfileRepository.findByUserRoleIn(userRole);
    }

    /** Validate Listof String and return ENUM values of UserRole.
     * @param userRole UserRole(JUNIOR,MENTOR etc) name you are looking for
     * @return list of UserProfile
     * @throws ResourceNotFoundException if userRole isn't valid
     */
    public List<UserRole> validateAndReturnAsEnum(List<String> userRole) {
        for (String value : userRole) {
            if (!EnumUtils.isValidEnumIgnoreCase(UserRole.class, value)) {
                throw new BadRequestException("Pick value from List");
            }
        }
        return userRole.stream()
                .map(String::toUpperCase)
                .map(UserRole::valueOf).collect(Collectors.toList());
    }
}
