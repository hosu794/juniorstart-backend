package com.juniorstart.juniorstart.service;

import com.juniorstart.juniorstart.exception.ResourceNotFoundException;
import com.juniorstart.juniorstart.model.AuthProvider;
import com.juniorstart.juniorstart.model.User;
import com.juniorstart.juniorstart.payload.ApiResponse;
import com.juniorstart.juniorstart.payload.ChangeMailRequest;
import com.juniorstart.juniorstart.repository.UserDao;
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
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


/** Represents an user service.
 * @author Dawid Wit
 * @version 1.1
 * @since 1.0
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {
    @Mock
    UserDao userDao;

    @InjectMocks
    UserService userService;

    User user;

    /** Create user for further tests.
     */
    @Before
    public void setUp() {
        user = new User();
        user.setPrivateId(new UUID(1,5));
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
        Mockito.when(userDao.findByPrivateIdAndPassword(user.getPrivateId(), "Password")).thenReturn(optional);
        ChangeMailRequest mailRequest = new ChangeMailRequest("test2@test.com",user.getPrivateId(),"Password");

        //When
        ResponseEntity<ApiResponse> isChanged = userService.changeEmail(mailRequest);
        User user = userDao.findByPrivateIdAndPassword(this.user.getPrivateId(),"Password").get();

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
        Mockito.when(userDao.findByPrivateIdAndPassword(this.user.getPrivateId(), "Password")).thenReturn(optional);
        ChangeMailRequest mailRequest = new ChangeMailRequest("test2@test.com",this.user.getPrivateId(),"Password");

        //When
        ResponseEntity<ApiResponse> isChanged = userService.changeEmail(mailRequest);

        //Then
        assertTrue(isChanged.getBody().isSuccess());
    }


}