package com.juniorstart.juniorstart.service;

import com.juniorstart.juniorstart.exception.BadRequestException;
import com.juniorstart.juniorstart.exception.ResourceNotFoundException;
import com.juniorstart.juniorstart.model.*;
import com.juniorstart.juniorstart.repository.EmploymentHistoryRepository;
import com.juniorstart.juniorstart.repository.UserProfileRepository;
import com.juniorstart.juniorstart.security.UserPrincipal;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.sql.Date;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EmploymentHistoryServiceTest {

    @Spy
    UserProfileRepository userProfileRepository;
    @Spy
    EmploymentHistoryRepository employmentHistoryRepository;
    @InjectMocks
    EmploymentHistoryService employmentHistoryService;

    User user;
    UserProfile userProfile;
    UserPrincipal userPrincipal;
    EmploymentHistory employmentHistory;

    Date startDate = Date.valueOf("2020-10-01");
    Date endDate = Date.valueOf("2020-10-30");

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

        employmentHistory = EmploymentHistory.builder().id(0).dateStartOfEmployment(startDate)
                .companyName("companyName").tasksAtWork("Task1").isCurrentEmployment(false).build();
    }

    @Test
    public void should_addEmploymentHistory() {

        employmentHistory.setDateEndOfEmployment(endDate);

        Mockito.when(userProfileRepository.findByPrivateId(ArgumentMatchers.any(UUID.class))).thenReturn(Optional.of(userProfile));

        ResponseEntity<?> status = employmentHistoryService.addEmploymentHistory(employmentHistory, userPrincipal);

        verify(userProfileRepository, times(1)).save(userProfile);
        assertThat(status.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void should_addRecentEmployer() {

        employmentHistory.setIsCurrentEmployment(true);

        Mockito.when(userProfileRepository.findByPrivateId(ArgumentMatchers.any(UUID.class))).thenReturn(Optional.of(userProfile));

        ResponseEntity<?> status = employmentHistoryService.addEmploymentHistory(employmentHistory, userPrincipal);

        verify(userProfileRepository, times(1)).save(userProfile);
        assertThat(status.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void should_updateEmploymentHistory() {

        Mockito.when(userProfileRepository.findByPrivateId(ArgumentMatchers.any(UUID.class))).thenReturn(Optional.of(userProfile));

        employmentHistoryService.addEmploymentHistory(employmentHistory, userPrincipal);

        employmentHistory.setCompanyName("OtherCompanyName");

        ResponseEntity<?> status = employmentHistoryService.updateEmploymentHistory(employmentHistory, userPrincipal);

        verify(userProfileRepository, times(2)).save(userProfile);
        assertThat(status.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void should_throwExceptionIsCurrentEmployer() {

        employmentHistory.setDateEndOfEmployment(endDate);
        employmentHistory.setIsCurrentEmployment(true);

        Mockito.when(userProfileRepository.findByPrivateId(ArgumentMatchers.any(UUID.class))).thenReturn(Optional.of(userProfile));

        Exception exception = assertThrows(
                BadRequestException.class,
                () -> employmentHistoryService.addEmploymentHistory(employmentHistory, userPrincipal),
                "Excepted doThing() to throw, but it didn't");

        assertEquals("You can't fill end date at current employer", exception.getMessage());
    }

    @Test
    public void should_Delete_EmploymentHistory() {

        employmentHistory.setDateEndOfEmployment(endDate);

        Mockito.when(userProfileRepository.findByPrivateId(ArgumentMatchers.any(UUID.class))).thenReturn(Optional.of(userProfile));

        employmentHistoryService.addEmploymentHistory(employmentHistory, userPrincipal);

        ResponseEntity<?> status = employmentHistoryService.deleteEmploymentHistory(employmentHistory, userPrincipal);

        verify(employmentHistoryRepository, times(1)).deleteById(ArgumentMatchers.anyLong());

        assertThat(status.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void should_ThrowExceptionWhenDelete_EmploymentHistory() {

        employmentHistory.setDateEndOfEmployment(endDate);

        Mockito.when(userProfileRepository.findByPrivateId(ArgumentMatchers.any(UUID.class))).thenReturn(Optional.of(userProfile));

        employmentHistory.setCompanyName("OtherCompanyName");

        Exception exception = assertThrows(
                ResourceNotFoundException.class,
                () ->  employmentHistoryService.deleteEmploymentHistory(employmentHistory, userPrincipal),
                "Expected doThing() to throw, but it didn't");

        assertEquals("EmploymentHistory not found with ID : '"+ employmentHistory.getId()+ "'", exception.getMessage());
    }
}
