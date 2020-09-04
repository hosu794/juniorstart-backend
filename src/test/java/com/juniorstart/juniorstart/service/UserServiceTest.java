package com.juniorstart.juniorstart.service;

import com.juniorstart.juniorstart.exception.ResourceNotFoundException;
import com.juniorstart.juniorstart.model.AuthProvider;
import com.juniorstart.juniorstart.model.User;
import com.juniorstart.juniorstart.payload.ApiResponse;
import com.juniorstart.juniorstart.payload.ChangeMailRequest;
import com.juniorstart.juniorstart.repository.UserDao;
import com.juniorstart.juniorstart.security.UserPrincipal;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

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


    UserDao userDao = Mockito.mock(UserDao.class);
    UserService userService = new UserService(userDao);
    UUID uuid = UUID.randomUUID();



    User user;
    UserPrincipal userPrincipal;
    Instant createdAt;


    /** Create user for further tests.
     */

    @Before
    public void initialize() throws Exception {
        createdAt = new SimpleDateFormat("yyyy-MM-dd").parse("2020-12-31").toInstant();
        user = new User();
        user.setPassword("Password");
        user.setPublicId(12l);
        user.setProvider(AuthProvider.local);
        user.setEmail("grzechu@gmail.com");
        user.setName("okioki");
        user.setEmailVerified(true);
        user.setPrivateId(uuid);
        userPrincipal = UserPrincipal.create(user);


    }

    @Test
    public void should_getCurrentUser() throws Exception {

        Mockito.when(userDao.findByPrivateId(ArgumentMatchers.any())).thenReturn(Optional.of(user));
        Assert.assertEquals(userService.getCurrentUser(userPrincipal).getEmail(),user.getEmail());
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
        ChangeMailRequest mailRequest = new ChangeMailRequest("test2@test.com","Test","Password");

        //When
        ResponseEntity<ApiResponse> isChanged = userService.changeEmail(mailRequest);
        User user = userDao.findByNameAndPassword("Test","Password").get();

        //Then
        assertTrue(isChanged.getBody().isSuccess());
        assertEquals("testChangeEmail Success","test2@test.com",user.getEmail());
    }

    /** Test of valid name and password.
     */
    @Test(expected = ResourceNotFoundException.class)
    public void validTestChangeEmail() {
        //Given
        Optional<User> optional = Optional.empty();
        Mockito.when(userDao.findByNameAndPassword("Test", "Password")).thenReturn(optional);
        ChangeMailRequest mailRequest = new ChangeMailRequest("test2@test.com","Test","Password");

        //When
        ResponseEntity<ApiResponse> isChanged = userService.changeEmail(mailRequest);

        //Then
        assertTrue(isChanged.getBody().isSuccess());
    }





}