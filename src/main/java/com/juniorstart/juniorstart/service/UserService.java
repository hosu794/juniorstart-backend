package com.juniorstart.juniorstart.service;

import com.juniorstart.juniorstart.exception.AgeSpecifierNotFoundException;
import com.juniorstart.juniorstart.exception.ResourceNotFoundException;
import com.juniorstart.juniorstart.model.User;
import com.juniorstart.juniorstart.util.numberParser.UrlNumberParser;
import com.juniorstart.juniorstart.payload.ApiResponse;
import com.juniorstart.juniorstart.payload.ChangeMailRequest;
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
 * @version 1.1
 * @since 1.0
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
     * @param changeMail class for change mail request with new email, name and password data.
     * @return ResponseEntity<Boolean> is email has changed in repo.
     * @throws ResourceNotFoundException cannot find user by name and password.
     */
    public ResponseEntity<ApiResponse> changeEmail(ChangeMailRequest changeMail) {
        User user = userDao.findByNameAndPassword(changeMail.getName(), changeMail.getPassword())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", changeMail.getName()));
        user.setEmail(changeMail.getEmail());
        user = userDao.save(user);
        return ResponseEntity.ok(new ApiResponse(changeMail.getEmail().equals(user.getEmail()), "Email change"));
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
}
