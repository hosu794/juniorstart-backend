package com.juniorstart.juniorstart.tmp;


import com.juniorstart.juniorstart.model.*;
import com.juniorstart.juniorstart.repository.UserDao;
import com.juniorstart.juniorstart.repository.UserProfileRepository;
import com.juniorstart.juniorstart.repository.UserTechnologyRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.*;

import com.juniorstart.juniorstart.service.UserProfileService;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
public class UserProfileTest {

    //@MockBean
   // private UserProfileService userProfileService;

    private UserProfileService userProfileService;

    @Autowired
    private UserDao userRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private UserTechnologyRepository userTechnologyRepository;


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
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
    public void createUser(){
        List<String> technologyList  = new ArrayList<>();
        technologyList.add("Java");

        List<UserProfile> foundUser5 = userProfileRepository.findByUserTechnology_technologyNameInAndUserRole(technologyList, ListUserRole.valueOf("MENTOR"));
        List<UserProfile> foundUser6 = userProfileService.findByTechnologyAndRole(technologyList, "MENTOR");
        System.out.println("5555555" + foundUser5 .toString());
        System.out.println("66666" + foundUser6 .toString());

    }
}
