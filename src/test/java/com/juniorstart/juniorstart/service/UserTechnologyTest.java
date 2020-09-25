package com.juniorstart.juniorstart.service;

import com.juniorstart.juniorstart.model.*;
import com.juniorstart.juniorstart.repository.UserDao;
import com.juniorstart.juniorstart.repository.UserProfileRepository;
import com.juniorstart.juniorstart.repository.UserTechnologyRepository;
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
public class UserTechnologyTest {

    @Spy
    UserDao userRepository;
    @Spy
    UserProfileRepository userProfileRepository;
    @Spy
    UserTechnologyRepository userTechnologyRepository;
    @InjectMocks
    UserProfileService userProfileService;
  //  @InjectMocks
 //   UserProfileTechnologyService userProfileTechnologyService;

    User user;
    User mockUser;
    UserProfile userProfile;
    UserProfile mockUserProfile;
    UserTechnology userTechnology1;
   // UserTechnology userTechnology2;


    UUID example_UUID = UUID.randomUUID();



    private Set<UserTechnology> listOfTechnology = new HashSet<>();

    @BeforeEach
    public void initializeNewList(){
        listOfTechnology = new HashSet<>();
        userTechnology1 = new UserTechnology();


    }

    @BeforeAll
    public void setUp() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        MockitoAnnotations.initMocks(this);

        User user;
        user = User.builder()
                .privateId(example_UUID)
                .publicId(10L)
                .name("Test")
                .email("test2@test.com")
                .emailVerified(true)
                .password("Test%123")
                .provider(AuthProvider.google).build();

        mockUser = user.getClass().getConstructor().newInstance();


        UserProfile userProfile = new UserProfile();
        userProfile.setPrivateId(example_UUID);
        userProfile.setUser(user);
        userProfile.setUserRole(UserRole.MENTOR);

        mockUserProfile = userProfile.getClass().getConstructor().newInstance();


        userRepository.save(user);
        userProfileRepository.save(userProfile);

        Mockito.when(userRepository.save(this.user)).thenReturn(mockUser);
        Mockito.when(userProfileRepository.save(this.userProfile)).thenReturn(mockUserProfile);

        System.out.println(userProfile.getPrivateId());
        System.out.println("333" + mockUserProfile.getPrivateId());


    }


    @Test
    public void shouldAddOneUserTechnology(){
        userTechnology1.setTechnologyName("technologia1");
        userTechnology1.setId(1L);

        listOfTechnology.add(userTechnology1);

        mockUserProfile.addUserManyTechnology(listOfTechnology);

        Mockito.when(userProfileRepository.save(mockUserProfile)).thenReturn(mockUserProfile);

        System.out.println(mockUserProfile.getUserTechnology());
        System.out.println(mockUserProfile.getPrivateId());


    }



    @Test
    public void shouldAddManyUserTechnology(){



    }





}




/*
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


        // UserTechnology userTechnology1;

        //  UserTechnology userTechnology1 = new UserTechnology();
        //    userTechnology1.setTechnologyName("Java");

        UserTechnology userTechnology1 = new UserTechnology();
        userTechnology1.setTechnologyName("technologia1");
        UserTechnology userTechnology2 = new UserTechnology();
        userTechnology2.setTechnologyName("technologia2");
        listOfTechnology.add(userTechnology1);
        listOfTechnology.add(userTechnology2);

        // System.out.println("65656565656" + userTechnology2.toString());
        //  System.out.println("65656565656" + listOfTechnology.toString());


        UserProfile userProfile = new UserProfile();
        userProfile.setUser(user);
        userProfile.setUserRole(UserRole.MENTOR);
        //userProfile.addUserTechnology(userTechnology);
        userProfile.addUserManyTechnology(listOfTechnology);

        userRepository.save(user);
        userProfileRepository.save(userProfile);


        mockUserProfile = userProfile.getClass().getConstructor().newInstance();

        Mockito.when(userRepository.save(this.user)).thenReturn(mockUser);
        Mockito.when(userProfileRepository.save(this.userProfile)).thenReturn(mockUserProfile);



        System.out.println("33333333333333333");
        // System.out.println(mockUserProfile);
        System.out.println(userProfile.getUserTechnology());
        //   System.out.println("4444");
        System.out.println(userProfile);
        System.out.println("77777777" + userTechnologyRepository.findAll());

        //System.out.println(mockUserProfile.getUserTechnology().toString());
    }
 */