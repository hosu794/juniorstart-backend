package com.juniorstart.juniorstart.service;

import com.juniorstart.juniorstart.exception.BadRequestException;
import com.juniorstart.juniorstart.exception.ResourceNotFoundException;
import com.juniorstart.juniorstart.model.UserRole;
import com.juniorstart.juniorstart.model.UserProfile;
import com.juniorstart.juniorstart.payload.PagedResponse;
import com.juniorstart.juniorstart.payload.UserRoleOrTechnologyRequest;
import com.juniorstart.juniorstart.repository.UserProfileRepository;

import com.juniorstart.juniorstart.util.ValidatePageUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.text.WordUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
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
     * @param userRoleOrTechnologyRequest Technology or UserRole you are looking for
     * @return list of UserProfile
     */
    public PagedResponse<UserProfile> selectionForSearching(UserRoleOrTechnologyRequest userRoleOrTechnologyRequest, int page, int size){
        List<String> technology = userRoleOrTechnologyRequest.getTechnology();
        List<String> userRole = userRoleOrTechnologyRequest.getUserRole();

        if (!technology.isEmpty() && !userRole.isEmpty()){
            List<UserRole> convertedUserRole= validateAndReturnAsEnum(userRole);
            return findByTechnologyAndRole(technology, convertedUserRole, page, size );
        }
        else if (!technology.isEmpty()){
            return findByTechnology(technology, page, size);
        }
        else{
            List<UserRole> convertedUserRole= validateAndReturnAsEnum(userRole);
            return findByUserRole(convertedUserRole, page, size);
        }
    }

    /** Get a List of UserProfile.
     * @param technology Technology name you are looking for
     * @param userRole UserRole(JUNIOR,MENTOR etc) name you are looking for
     * @return list of UserProfile
     * @throws ResourceNotFoundException if userRole isn't valid
     */
    public PagedResponse<UserProfile> findByTechnologyAndRole(List<String> technology, List<UserRole> userRole, int page, int size) {
        List<String> convertedTechnology = technology.stream().map(WordUtils::capitalize).collect(Collectors.toList());
        size = ValidatePageUtil.validatePageNumberAndSize(page, size);

        Pageable pageable = PageRequest.of(page, size);
        Page<UserProfile> foundUsers = userProfileRepository.findByUserTechnology_technologyNameInAndUserRoleIn(convertedTechnology, userRole, pageable);

        return getUserProfilePagedResponse(foundUsers);
    }

    private PagedResponse<UserProfile> getUserProfilePagedResponse(Page<UserProfile> foundUsers) {
        if(foundUsers.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), foundUsers.getNumber(), foundUsers.getSize(), foundUsers.getTotalElements(), foundUsers.getTotalPages(), foundUsers.isLast());
        }

        return new PagedResponse<>(foundUsers.toList(), foundUsers.getNumber(), foundUsers.getSize(), foundUsers.getTotalElements(), foundUsers.getTotalPages(), foundUsers.isLast());
    }

    /** Get a List of UserProfile.
     * @param technology Technology name you are looking for
     * @return list of UserProfile
     */
    public PagedResponse<UserProfile> findByTechnology(List<String> technology, int page, int size) {
        List<String> convertedTechnology = technology.stream().map(WordUtils::capitalize).collect(Collectors.toList());
        size = ValidatePageUtil.validatePageNumberAndSize(page, size);

        Pageable pageable = PageRequest.of(page, size);
        Page<UserProfile> foundUsers = userProfileRepository.findByUserTechnology_technologyNameIn(convertedTechnology, pageable);
        return new PagedResponse<>(foundUsers.toList(), foundUsers.getNumber(), foundUsers.getSize(), foundUsers.getTotalElements(), foundUsers.getTotalPages(), foundUsers.isLast());
    }

    /** Get a List of UserProfile.
     * @param userRole UserRole(JUNIOR,MENTOR etc) name you are looking for
     * @return list of UserProfile
     * @throws ResourceNotFoundException if userRole isn't valid
     */
    public PagedResponse<UserProfile> findByUserRole(List<UserRole> userRole, int page, int size) {
        size = ValidatePageUtil.validatePageNumberAndSize(page, size);

        Pageable pageable = PageRequest.of(page, size);
        Page<UserProfile> foundUsers = userProfileRepository.findByUserRoleIn(userRole, pageable);

        return getUserProfilePagedResponse(foundUsers);
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
