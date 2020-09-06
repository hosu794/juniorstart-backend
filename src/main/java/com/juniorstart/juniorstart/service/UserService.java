package com.juniorstart.juniorstart.service;

import com.juniorstart.juniorstart.exception.AgeSpecifierNotFoundException;
import com.juniorstart.juniorstart.exception.ResourceNotFoundException;
import com.juniorstart.juniorstart.model.User;
import com.juniorstart.juniorstart.parser.UrlNumberParser;
import com.juniorstart.juniorstart.payload.ApiResponse;
import com.juniorstart.juniorstart.payload.ChangeMailRequest;
import com.juniorstart.juniorstart.payload.ChangePasswordRequest;
import com.juniorstart.juniorstart.payload.interfaces.InterfaceChangeRequest;
import com.juniorstart.juniorstart.repository.UserDao;
import static com.juniorstart.juniorstart.repository.UserSpecifications.*;
import com.juniorstart.juniorstart.security.CurrentUser;
import com.juniorstart.juniorstart.security.UserPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

/** Represents an user service.
 * @author Grzegorz Szczęsny
 * @author Dawid Wit
 * @version 1.2
 * @since 1.1
 */
@Service
@Slf4j
public class UserService {
    final private UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    /** Get a current user credentials.
     * @param userPrincipal The current logged user.
     * @return a current user credentials
     * @throws ResourceNotFoundException if user not find
     */
    public User getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
        return userDao.findByPrivateId(userPrincipal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));
    }

    /** Update user email.
     * @param changeMail class for change mail request with new email, privateId and password data.
     * @return ResponseEntity<ApiResponse> is email has changed in repo.
     * @throws ResourceNotFoundException cannot find user by name and password.
     */
    public ResponseEntity<ApiResponse> changeEmail(ChangeMailRequest changeMail) {
        User user = getUserForChange(changeMail);
        user.setEmail(changeMail.getEmail());
        user = userDao.save(user);
        return changeChangeRequest(changeMail.getEmail().equals(user.getEmail()),"Email");
    }

    /** Update user password.
     * @param changePassword class for change password request with new password, privateId and old password data.
     * @return ResponseEntity<ApiResponse> is password has changed in repo.
     * @throws ResourceNotFoundException cannot find user by name and password.
     */
    public ResponseEntity<ApiResponse> changePassword(ChangePasswordRequest changePassword) {
        User user = getUserForChange(changePassword);
        user.setPassword(changePassword.getNewPassword());
        user = userDao.save(user);
        return changeChangeRequest(changePassword.getNewPassword().equals(user.getPassword())," Password");
    }

    public Optional<User> getUserByPrivateId(UUID id) {
        return userDao.findByPrivateId(id);
    }

    public Optional<User> getUserByPublicId(Long id) {
        return userDao.findByPublicId(id);
    }

    public Optional<User> getUserByEmail(String email) {
        return userDao.findByEmail(email);
    }

    public Iterable<User> getUsersByProps(
            Optional<Long> publicId,
            Optional<String> name,
            Optional<String> age,
            Optional<String> email) {

        // This is just creating userSpecification to be able to use and, doing nothing to query
        Specification<User> userSpecification = Specification.where((Specification<User>) (root, criteriaQuery, criteriaBuilder) -> root.isNotNull());

        // Creating specification based on props. If prop is null than we're not including it in query
        publicId.ifPresent(var -> userSpecification.and(publicIdEquals(var)));
        name.ifPresent(var -> userSpecification.and(nameEquals(var)));
        age.ifPresent(var -> userSpecification.and(getSpecificationForAge(UrlNumberParser.parse(var))));
        email.ifPresent(var -> userSpecification.and(emailEquals(var)));

        return userDao.findAll(userSpecification);
    }

    private Specification<User> getSpecificationForAge(UrlNumberParser.UrlNumberParserResponse<Integer> numberParserResponse) {
        switch (numberParserResponse.getNumberSpecifier()) {
            case EQUAL:
                return ageEquals(numberParserResponse.getNumber());
            case NOT_EQUAL:
                return ageNotEqual(numberParserResponse.getNumber());
            case GRATER:
                return ageGraterThan(numberParserResponse.getNumber());
            case GRATER_EQUAL:
                return ageGraterEqualThan(numberParserResponse.getNumber());
            case LOWER:
                return ageLowerThan(numberParserResponse.getNumber());
            case LOWER_EQUAL:
                return ageLowerEqualThan(numberParserResponse.getNumber());
            default:
                throw new AgeSpecifierNotFoundException();
        }
    }

    public User save(User user) {
        return userDao.save(user);
    }

    /** Refactored code for getting user from Database.
     * @param changeRequest is interface common for all change user data requests.
     * @return User from repository to change chosen data.
     * @throws ResourceNotFoundException cannot find user by name and password.
     */
    private User getUserForChange(InterfaceChangeRequest changeRequest){
        return userDao.findByPrivateIdAndPassword(changeRequest.getPrivateId(), changeRequest.getPassword())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", changeRequest.getPrivateId()));
    }

    /** Refactored code for getting user from Database.
     * @param changeCondition is for checking isd date changed in repository .
     * @param parameter name of changed parameter.
     * @return ResponseEntity<ApiResponse> response for controller.
     * @throws RuntimeException when parameter hasn't change.
     */
    private ResponseEntity<ApiResponse> changeChangeRequest(boolean changeCondition,String parameter){
        if (changeCondition) {
            return ResponseEntity.ok(new ApiResponse(true,parameter + " has change"));
        }
        throw new RuntimeException(parameter + " hasn't change");
    }
}
