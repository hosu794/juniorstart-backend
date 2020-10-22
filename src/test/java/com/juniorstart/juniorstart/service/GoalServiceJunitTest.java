package com.juniorstart.juniorstart.service;

import com.juniorstart.juniorstart.exception.ResourceNotFoundException;
import com.juniorstart.juniorstart.exception.SaveException;
import com.juniorstart.juniorstart.model.AuthProvider;
import com.juniorstart.juniorstart.model.Goal;
import com.juniorstart.juniorstart.model.GoalType;
import com.juniorstart.juniorstart.model.User;
import com.juniorstart.juniorstart.model.audit.UserStatus;
import com.juniorstart.juniorstart.payload.ApiResponse;
import com.juniorstart.juniorstart.payload.GoalRequestUpdate;
import com.juniorstart.juniorstart.repository.GoalDao;
import com.juniorstart.juniorstart.repository.UserDao;
import com.juniorstart.juniorstart.util.ClassMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class GoalServiceJunitTest {
    private final String EMAIL = "test@test.com";
    private final String PASSWORD = "password";
    private final String NAME ="Test name";

    @Mock
    private UserDao userDao;

    @Mock
    private GoalDao goalDao;

    @InjectMocks
    private GoalService service;

    private User user;
    private Goal goal;
    private GoalRequestUpdate requestUpdate;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .privateId(UUID.randomUUID())
                .publicId(10000001L)
                .name("Test name")
                .email("test@mail.com")
                .password("Test password")
                .provider(AuthProvider.github)
                .userStatus(UserStatus.OPEN).build();

        goal = Goal.builder()
                .id(UUID.randomUUID())
                .name("Test goal name")
                .goalType(GoalType.PROJECT)
                .isEnded(false)
                .endDate(Date.valueOf(LocalDate.now()))
                .user(user).build();

        requestUpdate = new GoalRequestUpdate(EMAIL,PASSWORD, ClassMapper.mapToDto(goal),NAME);

        when(userDao.findByEmailAndPassword(anyString(),anyString())).thenReturn((Optional.of(user)));
    }

    @Test
    @Order(1)
    @DisplayName("Create Correct")
    void createCorrect() {
        //Given
        Goal testGoal = Goal.builder()
                .id(UUID.randomUUID())
                .name("Test new goal name")
                .goalType(GoalType.PROJECT)
                .isEnded(false)
                .endDate(Date.valueOf(LocalDate.now()))
                .user(user).build();

        List<Goal> goals = new ArrayList<>();
        goals.add(goal);

        //When
        when(goalDao.findAllByUser(any(User.class))).thenReturn(goals);
        ResponseEntity<ApiResponse> response = service.create(EMAIL, PASSWORD, ClassMapper.mapToDto(testGoal));

        //Then
        verify(goalDao, times(1)).save(any(Goal.class));
        verify(userDao,times(1)).findByEmailAndPassword(anyString(),anyString());
        verify(goalDao,times(1)).findAllByUser(any(User.class));

        assertThat(response.getBody().isSuccess()).isEqualTo(true);
        assertThat(response.getBody().getMessage()).isEqualTo("Goal has been created");
    }

    @Test
    @Order(2)
    @DisplayName("Create valid duplicate")
    void createValidDuplicate() {
        //Given
        List<Goal> goals = new ArrayList<>();
        goals.add(goal);

        //When
        when(goalDao.findAllByUser(any(User.class))).thenReturn(goals);
        ResponseEntity<ApiResponse> response = service.create(EMAIL, PASSWORD, ClassMapper.mapToDto(goal));

        //Then
        verify(goalDao, times(0)).save(any(Goal.class));
        verify(userDao,times(1)).findByEmailAndPassword(anyString(),anyString());
        verify(goalDao,times(1)).findAllByUser(any(User.class));

        assertThat(response.getBody().isSuccess()).isEqualTo(false);
        assertThat(response.getBody().getMessage()).isEqualTo("Goal duplicate name");
    }

    @Test
    @Order(3)
    @DisplayName("Create valid user not found")
    void createUserNotFound(){
        //When
        when(userDao.findByEmailAndPassword(anyString(),anyString())).thenThrow(ResourceNotFoundException.class);

        //Then
        assertThrows(ResourceNotFoundException.class, () -> service.create(EMAIL, PASSWORD, ClassMapper.mapToDto(goal)));
    }

    @Test
    @Order(4)
    @DisplayName("Create valid goal save error")
    void createGoalSaveError(){
        //When
        when(goalDao.save(any(Goal.class))).thenThrow(SaveException.class);

        //Then
        assertThrows(SaveException.class, () -> service.create(EMAIL, PASSWORD, ClassMapper.mapToDto(goal)));
    }

    @Test
    @Order(5)
    @DisplayName("Get all correct")
    void getGoalsCorrect() {
        //Given
        List<Goal> goals = new ArrayList<>();
        goals.add(goal);

        //When
        when(goalDao.findAllByUser(any(User.class))).thenReturn(goals);

        ResponseEntity<List<Goal.GoalDto>> response = service.getGoals(EMAIL,PASSWORD);

        //Then
        verify(goalDao, times(1)).findAllByUser(any(User.class));
        assertThat(response.getBody().size()).isEqualTo(1);
    }

    @Test
    @Order(6)
    @DisplayName("Get all valid user not found")
    void getGoalsUserNotFound() {
        //When
        when(userDao.findByEmailAndPassword(anyString(),anyString())).thenThrow(ResourceNotFoundException.class);

        //Then
        assertThrows(ResourceNotFoundException.class, () -> service.getGoals(EMAIL,PASSWORD));
    }

    @Test
    @Order(7)
    @DisplayName("Update correct")
    void updateCorrect() {
        //When
        when(goalDao.findByNameAndUser(anyString(),any(User.class))).thenReturn(Optional.of(this.goal));
        ResponseEntity<ApiResponse> response = service.update(requestUpdate);

        //Then
        verify(goalDao, times(1)).findByNameAndUser(anyString(), any(User.class));
        assertThat(response.getBody().isSuccess()).isEqualTo(true);
        assertThat(response.getBody().getMessage()).isEqualTo("Goal has been updated");
    }

    @Test
    @Order(8)
    @DisplayName("Update valid user not found")
    void updateUserNotFound() {
        //When
        when(userDao.findByEmailAndPassword(anyString(),anyString())).thenThrow(ResourceNotFoundException.class);

        //Then
        assertThrows(ResourceNotFoundException.class, () -> service.update(requestUpdate));
    }

    @Test
    @Order(9)
    @DisplayName("Update valid goal not found")
    void updateGoalNotFound() {
        //When
        when(goalDao.findByNameAndUser(anyString(),any(User.class))).thenThrow(ResourceNotFoundException.class);

        //Then
        assertThrows(ResourceNotFoundException.class, () -> service.update(requestUpdate));
    }

    @Test
    @Order(10)
    @DisplayName("Delete correct")
    void deleteCorrect() {
        //When
        when(goalDao.findByNameAndUser(anyString(),any(User.class))).thenReturn(Optional.of(this.goal));
        ResponseEntity<ApiResponse> response = service.delete(EMAIL,PASSWORD,NAME);

        //Then
        verify(goalDao, times(1)).delete(any(Goal.class));
        assertThat(response.getBody().isSuccess()).isEqualTo(true);
        assertThat(response.getBody().getMessage()).isEqualTo("Goal with name : Test goal name has been deleted");
    }

    @Test
    @Order(11)
    @DisplayName("Delete valid user not found")
    void deleteUserNotFound() {
        //When
        when(userDao.findByEmailAndPassword(anyString(),anyString())).thenThrow(ResourceNotFoundException.class);

        //Then
        assertThrows(ResourceNotFoundException.class, () -> service.delete(EMAIL, PASSWORD, NAME));
    }

    @Test
    @Order(12)
    @DisplayName("Delete valid goal not found")
    void deleteGoalNotFound() {
        //When
        when(goalDao.findByNameAndUser(anyString(),any(User.class))).thenThrow(ResourceNotFoundException.class);

        //Then
        assertThrows(ResourceNotFoundException.class, () -> service.delete(EMAIL, PASSWORD, NAME));
    }
}