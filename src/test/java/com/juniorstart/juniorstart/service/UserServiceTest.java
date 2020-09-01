package com.juniorstart.juniorstart.service;

import com.juniorstart.juniorstart.model.AuthProvider;
import com.juniorstart.juniorstart.model.User;
import com.juniorstart.juniorstart.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {
    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;

    User user;

    @Before
    public void setUp() {
        user = new User();
        user.setId(5L);
        user.setName("Test");
        user.setEmail("test@test.com");
        user.setImageUrl("test Url");
        user.setEmailVerified(true);
        user.setPassword("Password");
        user.setProvider(AuthProvider.local);
        user.setProviderId("id");
    }

    @Test
    public void changeEmail() {
        //Given
        Optional<User> optional = Optional.of(this.user);
        User mockUser = this.user;
        mockUser.setEmail("test2@test.com");
        Mockito.when(userRepository.save(this.user)).thenReturn(mockUser);
        Mockito.when(userRepository.findByNameAndPassword("Test", "Password")).thenReturn(optional);

        //When
        boolean isChanged = userService.changeEmail("test2@test.com","Test","Password");
        User user = userRepository.findByNameAndPassword("Test","Password").get();

        //Then
        assertTrue(isChanged);
        assertEquals("test2@test.com",user.getEmail());
    }
}