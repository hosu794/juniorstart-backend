package com.juniorstart.juniorstart.service;


import com.juniorstart.juniorstart.exception.BadRequestException;
import com.juniorstart.juniorstart.model.*;
import com.juniorstart.juniorstart.payload.GetUserRoleOrTechnologyRequest;
import com.juniorstart.juniorstart.repository.UserDao;
import com.juniorstart.juniorstart.repository.UserProfileRepository;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserProfileMockTest {

    @Spy
    UserDao userRepository;
    @Spy
    UserProfileRepository userProfileRepository;
    @InjectMocks
    UserProfileService userProfileService;

    User user;
    User mockUser;
    UserProfile userProfile;
    UserProfile mockUserProfile;

    private List<String> technologyList  = new ArrayList<>();
    private List<String> userRoleList  = new ArrayList<>();
    private List<UserRole> userRoleListEnum  = new ArrayList<>();

    @BeforeEach
    public void initializeNewList(){
        technologyList  = new ArrayList<>();
        userRoleList  = new ArrayList<>();
        userRoleListEnum  = new ArrayList<>();
    }

    @BeforeAll
    public void setUp() throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        MockitoAnnotations.initMocks(this);

        User user;
        user = User.builder()
                .name("Test")
                .email("test2@test.com")
                .emailVerified(true)
                .password("Test%123")
                .provider(AuthProvider.google).build();

        mockUser = user.getClass().getConstructor().newInstance();

        UserTechnology userTechnology = new UserTechnology();
        userTechnology.setTechnologyName("Java");

        UserProfile userProfile = new UserProfile();
        userProfile.setUser(user);
        userProfile.setUserRole(UserRole.MENTOR);
        userProfile.addUserTechnology(userTechnology);

        mockUserProfile = userProfile.getClass().getConstructor().newInstance();

        Mockito.when(userRepository.save(this.user)).thenReturn(mockUser);
        Mockito.when(userProfileRepository.save(this.userProfile)).thenReturn(mockUserProfile);
    }

    @Test
    public void should_FindUserProfileByRoleAndTechnology() {
        technologyList.add("Java");
        userRoleList.add("MENTOR");
        userRoleListEnum.add(UserRole.MENTOR);
        GetUserRoleOrTechnologyRequest getUserRoleOrTechnologyRequest = new GetUserRoleOrTechnologyRequest(technologyList, userRoleList);

        Mockito.when(userProfileRepository.findByUserTechnology_technologyNameInAndUserRoleIn
                     (technologyList, userRoleListEnum)).thenReturn(Collections.singletonList(userProfile));

        assertEquals(userProfileService.selectionForSearching(getUserRoleOrTechnologyRequest), Collections.singletonList(userProfile));
    }


    @Test
    public void should_NotExistsFindUserProfileByRoleAndTechnology() {
        technologyList.add("TechnologyNotExist");
        GetUserRoleOrTechnologyRequest getUserRoleOrTechnologyRequest = new GetUserRoleOrTechnologyRequest(technologyList, userRoleList);

        Mockito.when(userProfileRepository.findByUserTechnology_technologyNameIn(technologyList)).thenReturn(Collections.singletonList(userProfile));

        assertNull(userProfile);
        assertEquals(Collections.singletonList(null), userProfileService.selectionForSearching(getUserRoleOrTechnologyRequest));
    }


    @Test
    public void should_NotExistsFindUserProfileByRoleAndTechnologyRole() {
        technologyList.add("Java");
        userRoleList.add("RoleDoesNotExist");
        GetUserRoleOrTechnologyRequest getUserRoleOrTechnologyRequest = new GetUserRoleOrTechnologyRequest(technologyList, userRoleList);

        Exception exception = assertThrows(
                BadRequestException.class,
                () -> userProfileService.selectionForSearching(getUserRoleOrTechnologyRequest),
                "Expected doThing() to throw, but it didn't"
        );
        assertEquals("Pick value from List", exception.getMessage());
    }

    @Test
    public void should_BeEmptyFindUserProfileByRoleAndTechnologyShouldBeEmpty() {
        technologyList.add("Java");
        userRoleList.add("JUNIOR");
        userRoleListEnum.add(UserRole.JUNIOR);
        GetUserRoleOrTechnologyRequest getUserRoleOrTechnologyRequest = new GetUserRoleOrTechnologyRequest(technologyList, userRoleList);

        List<UserProfile> foundUser = userProfileService.selectionForSearching(getUserRoleOrTechnologyRequest);
        Mockito.when(userProfileRepository.findByUserTechnology_technologyNameInAndUserRoleIn(technologyList, userRoleListEnum)).thenReturn(Collections.singletonList(userProfile));

        assertNull(userProfile);
        assertEquals(0, foundUser.size());
    }

    @Test
    public void should_FindUserProfileByRole() {
        userRoleList.add("MENTOR");
        userRoleListEnum.add((UserRole.MENTOR));
        GetUserRoleOrTechnologyRequest getUserRoleOrTechnologyRequest = new GetUserRoleOrTechnologyRequest(technologyList, userRoleList);

        Mockito.when(userProfileRepository.findByUserRoleIn(userRoleListEnum)).thenReturn(Collections.singletonList(userProfile));

        assertEquals(userProfileService.selectionForSearching(getUserRoleOrTechnologyRequest), Collections.singletonList(userProfile));
    }

    @Test
    public void should_FindUserProfileByTechnology() {
        technologyList.add("Java");
        GetUserRoleOrTechnologyRequest getUserRoleOrTechnologyRequest = new GetUserRoleOrTechnologyRequest(technologyList, userRoleList);

        Mockito.when(userProfileRepository.findByUserTechnology_technologyNameIn(technologyList)).thenReturn(Collections.singletonList(userProfile));

        assertEquals(userProfileService.selectionForSearching(getUserRoleOrTechnologyRequest), Collections.singletonList(userProfile));
    }

    @Test
    public void should_NotExistsFindUserProfileByRoleNotExist() {
        userRoleList.add("RoleDoesNotExist");
        GetUserRoleOrTechnologyRequest getUserRoleOrTechnologyRequest = new GetUserRoleOrTechnologyRequest(technologyList, userRoleList);

        Exception exception = assertThrows(
                BadRequestException.class,
                    () -> userProfileService.selectionForSearching(getUserRoleOrTechnologyRequest),
                "Expected doThing() to throw, but it didn't"
        );
        assertEquals("Pick value from List", exception.getMessage());
    }
}

