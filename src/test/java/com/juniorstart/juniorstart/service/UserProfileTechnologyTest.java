package com.juniorstart.juniorstart.service;

import com.juniorstart.juniorstart.model.*;
import com.juniorstart.juniorstart.repository.UserDao;
import com.juniorstart.juniorstart.repository.UserProfileRepository;
import com.juniorstart.juniorstart.repository.UserProfileTechnologyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserProfileTechnologyTest {


    @Spy
    UserDao userRepository;
    @Spy
    UserProfileRepository userProfileRepository;
    @Spy
    UserProfileTechnologyRepository userProfileTechnologyRepository;
    @InjectMocks
    UserProfileService userProfileService;
  //  @InjectMocks
 //   UserProfileTechnologyService userProfileTechnologyService;

    User user;
    User mockUser;
    UserProfile userProfile;
    UserProfile mockUserProfile;

    private List<String> technologyList = new ArrayList<>();

    private Set<UserTechnology> listOfTechnology = new HashSet<>();


    @BeforeEach
    public void initializeNewList(){
        technologyList = new ArrayList<>();
        listOfTechnology = new HashSet<>();
    }

    @BeforeAll
    public void setUp() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        MockitoAnnotations.initMocks(this);

        technologyList.add("technologia1");
        technologyList.add("technologia2");

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

        UserTechnology userTechnology1 = new UserTechnology();
        userTechnology.setTechnologyName("technologia1");
        UserTechnology userTechnology2 = new UserTechnology();
        userTechnology.setTechnologyName("technologia2");
        listOfTechnology.add(userTechnology1);
        listOfTechnology.add(userTechnology2);



        UserProfile userProfile = new UserProfile();
        userProfile.setUser(user);
        userProfile.setUserRole(UserRole.MENTOR);
        userProfile.addUserTechnology(userTechnology);
        userProfile.addUserManyTechnology(listOfTechnology);

        mockUserProfile = userProfile.getClass().getConstructor().newInstance();

        Mockito.when(userRepository.save(this.user)).thenReturn(mockUser);
        Mockito.when(userProfileRepository.save(this.userProfile)).thenReturn(mockUserProfile);

        System.out.println("33333333333333333");
        System.out.println(mockUserProfile);
        System.out.println(userProfile.getUserTechnology());
        System.out.println("4444");
        System.out.println(userProfile);
        System.out.println(userProfileTechnologyRepository.findAll());



        //System.out.println(mockUserProfile.getUserTechnology().toString());

    }


    @Test
    public void temp(){
        System.out.println("5");
    }



}
