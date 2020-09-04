package com.juniorstart.juniorstart.service;


import com.juniorstart.juniorstart.model.*;
import com.juniorstart.juniorstart.repository.UserDao;
import com.juniorstart.juniorstart.repository.UserProfileRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
//@Rollback(false)
//@Transactional
public class UserProfileTest {

    @MockBean
    private UserProfileService userProfileService;

    @Autowired
    private UserDao userRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this); // this is needed for inititalizytion of mocks, if you use @Mock

    }


    @Test
    public void createUser(){
        User user = new User();
        user.setName("test1");
        user.setEmail("test1@mail.pl");
        user.setEmailVerified(true);
        user.setPassword("Test%123");
        user.setProvider(AuthProvider.google);

        userRepository.save(user);
        System.out.println("11111111" + userRepository.findAll());



        UserTechnology userTechnology = new UserTechnology();
        userTechnology.setTechnology("Java");


        UserProfile userProfile = new UserProfile();
        userProfile.setUser(user);
        userProfile.setUserRole(ListUserRole.MENTOR);
        userProfile.addUserTechnology(userTechnology);

        userProfileRepository.save(userProfile);

        //userProfile.setUserTechnology(userTechnology);



        List<String> klop  = new ArrayList<>();
        klop.add("Java");

         List<UserProfile> foundUser = userProfileService.findMentorByTechnology(klop);
         System.out.println("222222" + foundUser.toString());


        List<UserProfile> foundUser2 = userProfileService.findOneMentorByTechnology("Java");
        System.out.println("222222" + foundUser2.toString());


     //   List<UserProfile> foundUser2 = userProfileRepository.findAllByUserTechnologyIn(klop);
     //   System.out.println("222222" + foundUser2.toString());


        List<UserProfile> tmp = userProfileRepository.findAll();
        System.out.println("33333333" + tmp.toString());











    }







}
