package com.juniorstart.juniorstart.service;

import com.juniorstart.juniorstart.email.Mail;
import com.juniorstart.juniorstart.email.MailService;
import com.juniorstart.juniorstart.exception.ResourceNotFoundException;
import com.juniorstart.juniorstart.model.AuthProvider;
import com.juniorstart.juniorstart.model.User;
import com.juniorstart.juniorstart.model.audit.UserStatus;
import com.juniorstart.juniorstart.payload.*;
import com.juniorstart.juniorstart.repository.UserDao;
import com.juniorstart.juniorstart.security.UserPrincipal;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/** Represents an user service.
 * @author Noboseki
 * @version 1.3
 * @since 1.2
 */
@SpringJUnitWebConfig
public class UserServiceTest {

    @Mock
    private UserDao userDao;
    @Mock
    private MailService mailService;
    @InjectMocks
    private UserService userService;
    private User user;
    private User user2;
    private User mockUser;
    private UserPrincipal userPrincipal;
    private ChangePasswordRequest passwordRequest;
    private ChangeMailRequest mailRequest;
    private ChangeStatusRequest statusRequest;
    private List<User> usersSummaries;
    private List<User> allUsers;

    @BeforeEach
    void setUp() throws IllegalAccessException, InstantiationException {

        user = User.builder()
                .privateId(UUID.randomUUID())
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

        user2 = User.builder()
                .privateId(UUID.randomUUID())
                .publicId(12l)
                .name("Test2")
                .age(12)
                .hiddenFromSearch(false)
                .email("test2@test.com")
                .imageUrl("test2Url")
                .emailVerified(true)
                .password("Password2")
                .provider(AuthProvider.local)
                .userStatus(UserStatus.OPEN)
                .providerId("id2").build();

        usersSummaries = new ArrayList<>();
        usersSummaries.add(user2);

        allUsers = new ArrayList<>();
        allUsers.add(user);
        allUsers.add(user2);

        mockUser = user.getClass().newInstance();
        userPrincipal = UserPrincipal.create(user);
        passwordRequest = new ChangePasswordRequest("NewPassword", user.getPrivateId(), "Password");
        mailRequest = new ChangeMailRequest("test2@test.com", user.getPrivateId(), "Password");
        statusRequest = new ChangeStatusRequest(UserStatus.OPEN, user.getPrivateId(), "Password");

        when(userDao.save(this.user)).thenReturn(mockUser);
        when(userDao.findByPrivateIdAndPassword(user.getPrivateId(), user.getPassword())).thenReturn(Optional.of(this.user));
    }

    @Test
    @DisplayName("Get Current User correct")
    public void should_getCurrentUser() {
        when(userDao.findByPrivateId(ArgumentMatchers.any())).thenReturn(Optional.of(user));
        Assert.assertEquals(userService.getCurrentUser(userPrincipal).getEmail(), user.getEmail());
    }

    @Test
    @DisplayName("Change mail correct")
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

    @Test
    @DisplayName("Change email valid")
    public void validTestChangeEmail() {
        //When
        when(userDao.findByPrivateIdAndPassword(this.user.getPrivateId(), "Password")).thenReturn(Optional.empty());

        //Then
        assertThrows(ResourceNotFoundException.class, () ->{
            userService.changeEmail(mailRequest);
        });
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

    @Test
    @DisplayName("Change password valid")
    public void testValidChangePassword() {
        //When
        when(userDao.findByPrivateIdAndPassword(user.getPrivateId(), user.getPassword())).thenReturn(Optional.empty());

        //Then
        assertThrows(ResourceNotFoundException.class, () ->{
            userService.changePassword(passwordRequest);
        });
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

    @Test
    @DisplayName("Change status valid")
    public void testChangeStatusValid() {
        //When
        when(userDao.findByPrivateIdAndPassword(user.getPrivateId(), user.getPassword())).thenReturn(Optional.empty());

        //Then
        assertThrows(ResourceNotFoundException.class, () ->{
            userService.changeStatus(statusRequest);
        });
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

    @Test
    @DisplayName("Get all user without current logged")
    public void should_return_findAllUserSummaries() throws Exception {
        when(userDao.findById(any(UUID.class))).thenReturn(Optional.of(user));
        when(userDao.findAll()).thenReturn(allUsers);

        List<UserSummary> response = userService.findAllUserSummaries(userPrincipal);

        assertEquals(response.get(0).getEmail(), usersSummaries.get(0).getEmail());
        assertEquals(response.get(0).getId(), usersSummaries.get(0).getPublicId());


        verify(userDao, times(1)).findById(any());
        verify(userDao, times(1)).findAll();
    }

}
