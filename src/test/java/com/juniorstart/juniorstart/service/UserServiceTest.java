package com.juniorstart.juniorstart.service;

import com.juniorstart.juniorstart.exception.ResourceNotFoundException;
import com.juniorstart.juniorstart.model.AuthProvider;
import com.juniorstart.juniorstart.model.User;
import com.juniorstart.juniorstart.repository.UserDaoImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


/** Represents an user service.
 * @author Dawid Wit
 * @version 1.0
 * @since 1.0
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {
    @Mock
    UserDaoImpl userDao;

    @InjectMocks
    UserService userService;

    User user;

    /** Create user for further tests.
     */
    @Before
    public void setUp() {
        user = new User();
        user.setName("Test");
        user.setEmail("test@test.com");
        user.setImageUrl("test Url");
        user.setEmailVerified(true);
        user.setPassword("Password");
        user.setProvider(AuthProvider.local);
        user.setProviderId("id");
    }

    /** Test of correct data.
     */
    @Test
    public void testChangeEmail() {
        //Given
        Optional<User> optional = Optional.of(this.user);
        User mockUser = this.user;
        mockUser.setEmail("test2@test.com");
        Mockito.when(userDao.save(this.user)).thenReturn(mockUser);
        Mockito.when(userDao.findByNameAndPassword("Test", "Password")).thenReturn(optional);

        //When
        ResponseEntity<Boolean> isChanged = userService.changeEmail("test2@test.com","Test","Password");
        User user = userDao.findByNameAndPassword("Test","Password").get();

        //Then
        assertTrue(isChanged.getBody());
        assertEquals("testChangeEmail Success","test2@test.com",user.getEmail());
    }

    /** Test of valid name and password.
     */
    @Test(expected = ResourceNotFoundException.class)
    public void validTestChangeEmail() {
        //Given
        Optional<User> optional = Optional.empty();
        Mockito.when(userDao.findByNameAndPassword("Test", "Password")).thenReturn(optional);

        //When
        ResponseEntity<Boolean> isChanged = userService.changeEmail("test2@test.com","Test","Password");

        //Then
        assertTrue(isChanged.getBody());
    }


}