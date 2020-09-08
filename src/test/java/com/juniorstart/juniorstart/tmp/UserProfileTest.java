package com.juniorstart.juniorstart.tmp;


import com.juniorstart.juniorstart.exception.BadRequestException;
import com.juniorstart.juniorstart.model.*;
import com.juniorstart.juniorstart.repository.UserDao;
import com.juniorstart.juniorstart.repository.UserProfileRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.*;
import com.juniorstart.juniorstart.service.UserProfileService;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
public class UserProfileTest {

    @SpyBean
    private UserProfileService userProfileService;

    @Autowired
    private UserDao userRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;


    List<String> technologyList  = new ArrayList<>();
    List<String> userRoleList  = new ArrayList<>();

    @Before
    public void setUp() {
        User user;

        user = User.builder()
                .name("Test")
                .email("test@test.com")
                .emailVerified(true)
                .password("Test%123")
                .provider(AuthProvider.google).build();
        userRepository.save(user);

        UserTechnology userTechnology = new UserTechnology();
        userTechnology.setTechnologyName("Java");

        UserProfile userProfile = new UserProfile();
        userProfile.setUser(user);
        userProfile.setUserRole(ListUserRole.MENTOR);
        userProfile.addUserTechnology(userTechnology);
        userProfileRepository.save(userProfile);
    }

    @Test
    public void whenFindUserProfileByRoleAndTechnologyShouldBeFound(){
        technologyList.add("java");
        userRoleList.add("MENTOR");
        List<UserProfile> foundUser = userProfileService.findByTechnologyAndRole(technologyList, userRoleList);
        assertTrue(foundUser.size() != 0);
    }

    @Test
    public void whenFindUserProfileByRoleAndTechnologyNotExista(){
        technologyList.add("TechnologyNotExist");
        List<UserProfile> userNotExist = userProfileService.findByTechnologyAndRole(technologyList, userRoleList);
        assertEquals(0, userNotExist.size());
    }

    @Test
    public void whenFindUserProfileByRoleAndTechnologyRoleDoesNotExist(){
        technologyList.add("Java");
        userRoleList.add("RoleDoesNotExist");

        Exception exception = assertThrows(
                BadRequestException.class, () -> {
                    userProfileService.findByTechnologyAndRole(technologyList, userRoleList);
                }
        );
        assertEquals("Pick value from List", exception.getMessage());
    }

    @Test
    public void whenFindUserProfileByRoleAndTechnologyShouldBeEmpty(){
        technologyList.add("Java");
        userRoleList.add("JUNIOR");

        List<UserProfile> foundUser = userProfileService.findByTechnologyAndRole(technologyList, userRoleList);
        assertEquals(0, foundUser.size());
    }

    @Test
    public void whenFindUserProfileByRole(){
        userRoleList.add("MENTOR");
        List<UserProfile> foundUser = userProfileService.findByUserRole(userRoleList);
        assertTrue(foundUser.size() != 0);
    }

    @Test
    public void whenFindUserProfileByTechnology(){
        technologyList.add("java");
        List<UserProfile> foundUser = userProfileService.findByTechnology(technologyList);
        assertTrue(foundUser.size() != 0);
    }

    @Test
    public void whenFindUserProfileByRoleNotExist(){
        userRoleList.add("RoleDoesNotExist");
        Exception exception = assertThrows(
                BadRequestException.class, () -> {
                    userProfileService.findByUserRole(userRoleList);
                }
        );
        assertEquals("Pick value from List", exception.getMessage());
    }

}







