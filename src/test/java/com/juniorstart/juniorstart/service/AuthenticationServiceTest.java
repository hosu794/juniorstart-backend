package com.juniorstart.juniorstart.service;


import com.juniorstart.juniorstart.model.AuthProvider;
import com.juniorstart.juniorstart.model.User;
import com.juniorstart.juniorstart.payload.LoginRequest;
import com.juniorstart.juniorstart.payload.SignUpRequest;
import com.juniorstart.juniorstart.repository.UserDao;
import com.juniorstart.juniorstart.security.TokenProvider;
import com.juniorstart.juniorstart.security.UserPrincipal;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Optional;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(MockitoJUnitRunner.class)
public class AuthenticationServiceTest {

    
     AuthenticationManager authenticationManager = Mockito.mock(AuthenticationManager.class);
     Authentication authentication = Mockito.mock(Authentication.class);

    PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);
    UserDao userDao = Mockito.mock(UserDao.class);

    TokenProvider tokenProvider = Mockito.mock(TokenProvider.class);
    AuthenticationService authenticationService = new AuthenticationService(authenticationManager, userDao, passwordEncoder, tokenProvider);

    User user;
    UserPrincipal userPrincipal;
    Instant createdAt;
    SignUpRequest signUpRequest;
    LoginRequest loginRequest;
    MockHttpServletRequest request;


    @Before
    public void initialize() throws Exception {
        createdAt = new SimpleDateFormat("yyyy-MM-dd").parse("2020-12-31").toInstant();
        user = new User();
        user.setPassword("Password");
        user.setPublicId((long) 12);
        user.setProvider(AuthProvider.local);
        user.setEmail("grzechu@gmail.com");
        user.setName("okioki");
        user.setEmailVerified(true);
        userPrincipal = UserPrincipal.create(user);
        signUpRequest = new SignUpRequest();
        signUpRequest.setEmail(user.getEmail());
        signUpRequest.setName(user.getName());
        signUpRequest.setPassword(user.getPassword());
        loginRequest = new LoginRequest();
        loginRequest.setEmail(user.getEmail());
        loginRequest.setPassword(user.getPassword());
        request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    @Test
    public void should_registerNewUser() throws Exception {


        Mockito.when(userDao.findByEmail(ArgumentMatchers.any(String.class))).thenReturn(Optional.empty());
        Mockito.when(userDao.save(ArgumentMatchers.any(User.class))).thenReturn(user);
        Assert.assertEquals(authenticationService.registerUser(signUpRequest).getStatusCode(), HttpStatus.CREATED);
    }

    @Test
    public void should_authenticateUser() throws Exception {
        Mockito.when(tokenProvider.createToken(ArgumentMatchers.any(Authentication.class))).thenReturn("Token");
        Mockito.when(authentication.getPrincipal()).thenReturn(userPrincipal);
        Mockito.when(authenticationManager.authenticate(ArgumentMatchers.any())).thenReturn(authentication);


        Assert.assertEquals(authenticationService.authenticationUser(loginRequest).getStatusCode(), HttpStatus.OK);
    }

}
