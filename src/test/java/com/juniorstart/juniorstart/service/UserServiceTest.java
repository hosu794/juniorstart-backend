package com.juniorstart.juniorstart.service;

import com.juniorstart.juniorstart.email.Mail;
import com.juniorstart.juniorstart.email.MailService;
import com.juniorstart.juniorstart.exception.ResourceNotFoundException;
import com.juniorstart.juniorstart.model.AuthProvider;
import com.juniorstart.juniorstart.model.User;
import com.juniorstart.juniorstart.model.audit.UserStatus;
import com.juniorstart.juniorstart.payload.ApiResponse;
import com.juniorstart.juniorstart.payload.ChangeMailRequest;
import com.juniorstart.juniorstart.payload.ChangePasswordRequest;
import com.juniorstart.juniorstart.payload.ChangeStatusRequest;
import com.juniorstart.juniorstart.repository.UserDao;
import com.juniorstart.juniorstart.security.UserPrincipal;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/** Represents an user service.
 * @author Dawid Wit
 * @version 1.3
 * @since 1.2
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Mock
    private UserDao userDao;
    @Mock
    private MailService mailService;
    @InjectMocks
    private UserService userService;
    private UUID uuid = UUID.randomUUID();
    private User user;
    private User mockUser;
    private UserPrincipal userPrincipal;
    private ChangePasswordRequest passwordRequest;
    private ChangeMailRequest mailRequest;
    private ChangeStatusRequest statusRequest;

    /**
     * Create user for further tests.
     */
    @Before
    public void initialize() throws IllegalAccessException, InstantiationException {
        user = User.builder()
                .privateId(uuid)
                .publicId(10L)
                .name("Test")
                .age(18)
                .hiddenFromSearch(false)
                .email("test@test.com")
                .imageUrl("test Url")
                .emailVerified(true)
                .password("Password")
                .provider(AuthProvider.local)
                .userStatus(UserStatus.OPEN)
                .providerId("id").build();

        mockUser = user.getClass().newInstance();
        userPrincipal = UserPrincipal.create(user);
        passwordRequest = new ChangePasswordRequest("NewPassword", user.getPrivateId(), "Password");
        mailRequest = new ChangeMailRequest("test2@test.com", user.getPrivateId(), "Password");
        statusRequest = new ChangeStatusRequest(UserStatus.OPEN, user.getPrivateId(), "Password");

        Mockito.when(userDao.save(this.user)).thenReturn(mockUser);
        Mockito.when(userDao.findByPrivateIdAndPassword(user.getPrivateId(), user.getPassword())).thenReturn(Optional.of(this.user));
    }

    @Test
    public void should_getCurrentUser() {
        Mockito.when(userDao.findByPrivateId(ArgumentMatchers.any())).thenReturn(Optional.of(user));
        Assert.assertEquals(userService.getCurrentUser(userPrincipal).getEmail(), user.getEmail());
    }

    @Test
    @DisplayName("Change mail sucess")
    public void testChangeEmail() {
        //Given
        mockUser.setEmail("test2@test.com");

        //When
        ResponseEntity<ApiResponse> isChanged = userService.changeEmail(mailRequest);
        User user = userDao.findByPrivateIdAndPassword(this.user.getPrivateId(), "Password").get();

        //Then
        assertTrue(isChanged.getBody().isSuccess());
        assertEquals("testChangeEmail Success", "test2@test.com", user.getEmail());
        verify(userDao, times(1)).save(this.user);
        verify(mailService, times(1)).send(any(Mail.class));
    }

    @Test(expected = ResourceNotFoundException.class)
    @DisplayName("Change email valid")
    public void validTestChangeEmail() {
        //Given
        Mockito.when(userDao.findByPrivateIdAndPassword(this.user.getPrivateId(), "Password")).thenReturn(Optional.empty());

        //When
        ResponseEntity<ApiResponse> isChanged = userService.changeEmail(mailRequest);

        //Then
        assertTrue(isChanged.getBody().isSuccess());
        verify(userDao, times(1)).save(this.user);
        verify(mailService, times(1)).send(any(Mail.class));
    }

    @Test
    @DisplayName("Change password correct")
    public void testChangePassword() {
        //Given
        mockUser.setPassword("NewPassword");

        //When
        ResponseEntity<ApiResponse> isChanged = userService.changePassword(passwordRequest);

        //Then
        assertTrue(isChanged.getBody().isSuccess());
        verify(userDao, times(1)).save(this.user);
        verify(mailService, times(1)).send(any(Mail.class));
    }

    @Test(expected = ResourceNotFoundException.class)
    @DisplayName("Change password valid")
    public void testValidChangePassword() {
        //When
        Mockito.when(userDao.findByPrivateIdAndPassword(user.getPrivateId(), user.getPassword())).thenReturn(Optional.empty());
        ResponseEntity<ApiResponse> isChanged = userService.changePassword(passwordRequest);

        //Then
        assertTrue(isChanged.getBody().isSuccess());
        verify(userDao, times(1)).save(this.user);
        verify(mailService, times(1)).send(any(Mail.class));
    }

    @Test
    @DisplayName("Change status correct")
    public void testChangeStatus() {
        //Given
        mockUser.setUserStatus(UserStatus.OPEN);

        //When
        ResponseEntity<ApiResponse> isChanged = userService.changeStatus(statusRequest);

        //Then
        assertTrue(isChanged.getBody().isSuccess());
        verify(userDao, times(1)).save(this.user);
        verify(mailService, never()).send(any(Mail.class));
    }

    @Test(expected = ResourceNotFoundException.class)
    @DisplayName("Change status valid")
    public void testChangeStatusValid() {
        //When
        Mockito.when(userDao.findByPrivateIdAndPassword(user.getPrivateId(), user.getPassword())).thenReturn(Optional.empty());
        ResponseEntity<ApiResponse> isChanged = userService.changeStatus(statusRequest);

        //Then
        assertTrue(isChanged.getBody().isSuccess());
        verify(userDao, times(1)).save(this.user);
        verify(mailService, never()).send(any(Mail.class));
    }

    @Test
    @DisplayName("Get status list Test")
    public void testGetStatusList() {
        //Given
        List<String> templateList = Arrays.asList("LOOKING_FOR_A_JOB", "HIRED", "FREELANCER", "OPEN");

        //When
        List<String> statusList = userService.getStatusList().getBody();

        //Then
        int i = 0;
        for (String s : statusList) {
            boolean isEquals = templateList.get(i).equals(statusList.get(i));
            assertTrue(isEquals);
        }
    }
}