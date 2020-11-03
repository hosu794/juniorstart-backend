package com.juniorstart.juniorstart.service;

import com.juniorstart.juniorstart.model.AuthProvider;
import com.juniorstart.juniorstart.model.User;
import com.juniorstart.juniorstart.model.UserProfile;
import com.juniorstart.juniorstart.repository.UserProfileRepository;
import com.juniorstart.juniorstart.security.UserPrincipal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserProfileAvatarServiceTest {

    @Spy
    UserProfileRepository userProfileRepository;
    @InjectMocks
    UserProfileAvatarService userProfileAvatarService;

    User user;
    UserProfile userProfile;
    UserPrincipal userPrincipal;

    UUID example_UUID = UUID.randomUUID();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        user = User.builder()
                .privateId(example_UUID)
                .publicId(10L)
                .name("Test")
                .email("test2@test.com")
                .emailVerified(true)
                .password("Test%123")
                .provider(AuthProvider.google).build();

        userPrincipal = UserPrincipal.create(user);

        userProfile = UserProfile.builder()
                .privateId(example_UUID)
                .user(user).build();
    }


    @Test
    public void should_adAvatar() {


        System.out.println("tak");
    }




}

