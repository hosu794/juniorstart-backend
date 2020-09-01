package com.juniorstart.juniorstart.service;

import com.juniorstart.juniorstart.model.AuthProvider;
import com.juniorstart.juniorstart.model.User;
import com.juniorstart.juniorstart.repository.UserRepository;
import com.juniorstart.juniorstart.security.UserPrincipal;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Optional;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    UserRepository userRepository = Mockito.mock(UserRepository.class);
    UserService userService = new UserService(userRepository);

    User user;
    UserPrincipal userPrincipal;
    Instant createdAt;

    @Before
    public void initialize() throws Exception {
        createdAt = new SimpleDateFormat("yyyy-MM-dd").parse("2020-12-31").toInstant();
        user = new User();
        user.setPassword("Password");
        user.setId((long) 12);
        user.setProvider(AuthProvider.local);
        user.setEmail("grzechu@gmail.com");
        user.setName("okioki");
        user.setEmailVerified(true);
        userPrincipal = UserPrincipal.create(user);

    }

    @Test
    public void should_getCurrentUser() throws Exception {

        Mockito.when(userRepository.findById(ArgumentMatchers.any(Long.class))).thenReturn(Optional.of(user));
        Assert.assertEquals(userService.getCurrentUser(userPrincipal).getEmail(),user.getEmail());
    }



}
