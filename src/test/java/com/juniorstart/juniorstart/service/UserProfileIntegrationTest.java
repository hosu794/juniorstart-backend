package com.juniorstart.juniorstart.service;

import com.juniorstart.juniorstart.exception.BadRequestException;
import com.juniorstart.juniorstart.model.*;
import com.juniorstart.juniorstart.payload.PagedResponse;
import com.juniorstart.juniorstart.payload.UserRoleOrTechnologyRequest;
import com.juniorstart.juniorstart.repository.UserDao;
import com.juniorstart.juniorstart.repository.UserProfileRepository;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;


/** Represents an user service.
 * @author Rafał Maduzia
 * @version 1.0
 */
@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class UserProfileIntegrationTest {

    @SpyBean
    private UserProfileService userProfileService;

    @Autowired
    private UserDao userRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    private List<String> technologyList  = new ArrayList<>();
    private List<UserRole> userRoleList  = new ArrayList<>();
    private List<String> userRoleListThrowException  = new ArrayList<>();
    private int page = 0;
    private int size = 8;

    @Before
    public void setUp() {
        User user;
        user = User.builder()
                .name("Test")
                .email("test2@test.com")
                .emailVerified(true)
                .password("Test%123")
                .provider(AuthProvider.google).build();
        userRepository.save(user);

        UserTechnology userTechnology = new UserTechnology();
        userTechnology.setTechnologyName("Java");

        UserProfile userProfile = new UserProfile();
        userProfile.setUser(user);
        userProfile.setUserRole(UserRole.MENTOR);

        Set<UserTechnology> userSetTechnology = new HashSet<>();
        userSetTechnology.add(userTechnology);

        userProfile.setUserTechnology(userSetTechnology);
        userProfileRepository.save(userProfile);
    }

    @Test
    public void should_FindUserProfileByRoleAndTechnology() {
        technologyList.add("java");
        userRoleList.add(UserRole.MENTOR);
        PagedResponse<UserProfile.UserProfileDto> foundUser = userProfileService.findByTechnologyAndRole(technologyList, userRoleList, page, size);
        assertNotEquals(new ArrayList<>(Collections.emptyList()), foundUser.getContent());
    }

    @Test
    public void should_NotExistsFindUserProfileByRoleAndTechnology() {
        technologyList.add("TechnologyNotExist");
        PagedResponse<UserProfile.UserProfileDto> userNotExist = userProfileService.findByTechnologyAndRole(technologyList, userRoleList, page, size);
        assertEquals(new ArrayList<>(Collections.emptyList()), userNotExist.getContent());
    }

    @Test
    public void should_BeEmptyFindUserProfileByRoleAndTechnologyShouldBeEmpty() {
        technologyList.add("Java");
        userRoleList.add(UserRole.JUNIOR);
        PagedResponse<UserProfile.UserProfileDto> foundUser = userProfileService.findByTechnologyAndRole(technologyList, userRoleList, page, size);
        assertEquals(new ArrayList<>(Collections.emptyList()), foundUser.getContent());
    }

    @Test
    public void should_FindUserProfileByRole() {
        userRoleList.add(UserRole.MENTOR);
        PagedResponse<UserProfile.UserProfileDto> foundUser = userProfileService.findByUserRole(userRoleList, page, size);
        assertNotEquals(new ArrayList<>(Collections.emptyList()), foundUser.getContent());
    }

    @Test
    public void should_FindUserProfileByTechnology() {
        technologyList.add("Java");
        PagedResponse<UserProfile.UserProfileDto> foundUser = userProfileService.findByTechnology(technologyList, page, size );
        assertNotEquals(new ArrayList<>(Collections.emptyList()), foundUser.getContent());
    }

    @Test
    public void should_NotExistsFindUserProfileByRoleAndTechnologyRole() {
        technologyList.add("Java");
        userRoleListThrowException.add("RoleDoesNotExist");

        UserRoleOrTechnologyRequest userRoleOrTechnologyRequest = new UserRoleOrTechnologyRequest(technologyList, userRoleListThrowException);

        Exception exception = assertThrows(
                BadRequestException.class, () -> {
                    userProfileService.selectionForSearching(userRoleOrTechnologyRequest, page, size);
                }
        );
        assertEquals("Pick value from List", exception.getMessage());
    }

    @Test
    public void should_NotExistsFindUserProfileByRoleNotExist() {
        userRoleListThrowException.add("RoleDoesNotExist");
        UserRoleOrTechnologyRequest userRoleOrTechnologyRequest = new UserRoleOrTechnologyRequest(technologyList, userRoleListThrowException);

        Exception exception = assertThrows(
                BadRequestException.class, () -> {
                    userProfileService.selectionForSearching(userRoleOrTechnologyRequest, page, size);
                }
        );
        assertEquals("Pick value from List", exception.getMessage());
    }
}