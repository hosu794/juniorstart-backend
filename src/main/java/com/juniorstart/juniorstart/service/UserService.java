package com.juniorstart.juniorstart.service;

import com.juniorstart.juniorstart.email.Mail;
import com.juniorstart.juniorstart.email.MailService;
import com.juniorstart.juniorstart.email.TemplateValues;
import com.juniorstart.juniorstart.exception.AgeSpecifierNotFoundException;
import com.juniorstart.juniorstart.exception.ResourceNotFoundException;
import com.juniorstart.juniorstart.model.User;
import com.juniorstart.juniorstart.model.audit.UserStatus;
import com.juniorstart.juniorstart.payload.*;
import com.juniorstart.juniorstart.payload.interfaces.InterfaceChangeRequest;
import com.juniorstart.juniorstart.repository.UserDao;
import com.juniorstart.juniorstart.security.CurrentUser;
import com.juniorstart.juniorstart.security.UserPrincipal;
import com.juniorstart.juniorstart.util.numberParser.UrlNumberParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.juniorstart.juniorstart.repository.UserSpecifications.*;

/** Represents an user service.
 * @author Grzegorz Szczęsny
 * @author Noboseki
 * @version 1.4
 * @since 1.3
 */
@Service
@Slf4j
public class UserService {
    final private UserDao userDao;
    final private MailService mailService;

    public UserService(UserDao userDao, MailService mailService) {
        this.userDao = userDao;
        this.mailService = mailService;
    }

    /** Get a current user credentials.
     * @param userPrincipal The current logged user.
     * @return a current user credentials
     * @throws ResourceNotFoundException if user not find
     */
    public UserSummary getCurrentUser(UserPrincipal userPrincipal) {
        User user = userDao.findByPrivateId(userPrincipal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));

        UserSummary userSummary = new UserSummary(user.getPublicId(), user.getName(), user.getEmail());

        return userSummary;
    }

    /** Update user email.
     * @param request class for change mail request with new email, privateId and password data.
     * @return ResponseEntity<ApiResponse> is email has changed in repo.
     * @throws ResourceNotFoundException cannot find user by name and password.
     */
    public ResponseEntity<ApiResponse> changeEmail(ChangeMailRequest request) {
        User user = getUserForChange(request);
        Mail mail = sendInformationEmile(user,"email","https://www.google.pl/", request.getEmail());
        user.setEmail(request.getEmail());
        user = userDao.save(user);
        return changeResponse(request.getEmail().equals(user.getEmail()),"Email", mail);
    }

    /** Update user password.
     * @param request class for change password request with new password, privateId and old password data.
     * @return ResponseEntity<ApiResponse> is password has changed in repo.
     * @throws ResourceNotFoundException cannot find user by name and password.
     */
    public ResponseEntity<ApiResponse> changePassword(ChangePasswordRequest request) {
        User user = getUserForChange(request);
        Mail mail = sendInformationEmile(user,"password","https://www.google.pl/",request.getNewPassword());
        user.setPassword(request.getNewPassword());
        user = userDao.save(user);
        return changeResponse(request.getNewPassword().equals(user.getPassword())," Password", mail);
    }

    /** Update user status.
     * @param request class for change status, request with new password, privateId and new status.
     * @return ResponseEntity<ApiResponse> is status has changed in repo.
     * @throws ResourceNotFoundException cannot find user by name and password.
     */
    public ResponseEntity<ApiResponse> changeStatus(ChangeStatusRequest request){
        User user = getUserForChange(request);
        user.setUserStatus(request.getUserStatus());
        user = userDao.save(user);
        return changeResponse(request.getUserStatus().equals(user.getUserStatus())," Status", null);
    }

    /** Method for getting all user statuses.
     */
    public ResponseEntity<List<String>> getStatusList(){
        List<String> statuses = Stream
                .of(UserStatus.values())
                .map(UserStatus::name)
                .collect(Collectors.toList());
        return ResponseEntity.ok(statuses);
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
     * @param mail for sending information emile.
     * @return ResponseEntity<ApiResponse> response for controller.
     * @throws RuntimeException when parameter hasn't change.
     */
    private ResponseEntity<ApiResponse> changeResponse(boolean changeCondition, String parameter, Mail mail) {
        if (changeCondition) {
            if (mail != null){
                mailService.send(mail);
            }
            return ResponseEntity.ok(new ApiResponse(true,parameter + " has change"));
        }
        throw new RuntimeException(parameter + " hasn't change");
    }

    /** Method for creating information email.
     * @param user User for whom the email is being sent.
     * @param data name of changed data.
     * @param value value of changed data.
     * @return Mail object send to user.
     */
    private Mail sendInformationEmile(User user, String data, String link, String value) {
        TemplateValues values = TemplateValues.builder()
                .changedData(data)
                .changeDataLink(link)
                .dataValue(user.getName())
                .name(value).build();

        return new Mail(user.getEmail(),values);
    }
}
