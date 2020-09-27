package com.juniorstart.juniorstart.service;

import com.juniorstart.juniorstart.exception.ResourceNotFoundException;
import com.juniorstart.juniorstart.model.Goal;
import com.juniorstart.juniorstart.model.User;
import com.juniorstart.juniorstart.payload.ApiResponse;
import com.juniorstart.juniorstart.payload.DeleteGoalRequest;
import com.juniorstart.juniorstart.payload.GetGoalRequest;
import com.juniorstart.juniorstart.repository.GoalDao;
import com.juniorstart.juniorstart.repository.UserDao;
import com.juniorstart.juniorstart.util.TestMethods;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.when;

@SpringJUnitWebConfig
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class GoalServiceTest {

    @Mock
    private GoalDao goalDao;
    @Mock
    private UserDao userDao;
    @InjectMocks
    private GoalService service;

    private User user;
    private Goal goal;

    @BeforeEach
    void setUp() {
        user = TestMethods.getUser();
        goal = TestMethods.getGoal();

        when(userDao.findByPrivateIdAndPassword(any(UUID.class), anyString())).thenReturn(Optional.of(this.user));
        when(goalDao.findById(any(UUID.class))).thenReturn(Optional.of(goal));
        when(goalDao.save(any(Goal.class)) ).thenReturn(goal);
    }

    @Test
    @Order(1)
    @DisplayName("create method correct")
    void createCorrect() {
        //When
        ResponseEntity<ApiResponse> response = service.create(new Goal().mapToDto(goal));

        //Then
        verify(goalDao,times(1)).save(any(Goal.class));
        assertThat(response.getBody().isSuccess()).isEqualTo(true);
        assertThat(response.getBody().getMessage()).isEqualTo("Goal has been created.");
    }

    @Test
    @Order(2)
    @DisplayName("getAll method correct")
    void getAll() {
        //Given
        Goal goalSet = TestMethods.getGoal();
        goalSet.setTitle("Test 2 Title");

        Set<Goal> dtos = new HashSet<>();
        dtos.add(goalSet);
        dtos.add(goal);

        //When
        GetGoalRequest getGoalRequest = new GetGoalRequest(UUID.randomUUID(),"Password");
        when(goalDao.findAllByUser(any(User.class))).thenReturn(dtos);

        ResponseEntity<Set<Goal.GoalDto>> response = service.getAll(getGoalRequest);

        //Then
        verify(userDao, times(1)).findByPrivateIdAndPassword(getGoalRequest.getUserPrivateId(),getGoalRequest.getUserPassword());
        verify(goalDao, times(1)).findAllByUser(any(User.class));
        assertThat(response.getBody()).hasSize(2);
    }

    @Test
    @Order(3)
    @DisplayName("getAll method Valid")
    void getAllValid() {
        //When
        when(userDao.findByPrivateIdAndPassword(any(UUID.class), anyString())).thenReturn(Optional.empty());
        GetGoalRequest getGoalRequest = new GetGoalRequest(UUID.randomUUID(),"Password");

        //Then
        assertThrows(ResourceNotFoundException.class, () ->{
            service.getAll(getGoalRequest);
        });
    }

    @Test
    @Order(4)
    @DisplayName("update method correct")
    void updateCorrect() {
        //When
        ResponseEntity<ApiResponse> response = service.update(new Goal().mapToDto(TestMethods.getGoal()));

        //Then
        verify(goalDao, times(1)).findById(any(UUID.class));
        verify(goalDao, times(1)).save(any(Goal.class));
        assertThat(response.getBody().isSuccess()).isEqualTo(true);
        assertThat(response.getBody().getMessage()).isEqualTo("Goal has been updated.");
    }

    @Test
    @Order(5)
    @DisplayName("update method valid")
    void updateValid() {
        //When
        when(goalDao.findById(any(UUID.class))).thenReturn(Optional.empty());

        //Then
        assertThrows(ResourceNotFoundException.class, () ->{
            service.update(new Goal().mapToDto(TestMethods.getGoal()));
        });
    }

    @Test
    @Order(6)
    @DisplayName("delete method correct")
    void deleteCorrect() {
        //Given
        DeleteGoalRequest request = new DeleteGoalRequest(UUID.randomUUID(), UUID.randomUUID(), "Password");
        goal.setUser(user);

        //When
        ResponseEntity<ApiResponse> response = service.delete(request);

        //Then
        verify(userDao,times(1)).findByPrivateIdAndPassword(request.getUserPrivateId(), request.getUserPassword());
        verify(goalDao, times(1)).findById(any(UUID.class));
        assertThat(response.getBody().isSuccess()).isEqualTo(true);
        assertThat(response.getBody().getMessage()).isEqualTo("Goal has been deleted");
    }

    @Test
    @Order(7)
    @DisplayName("delete method valid goalId")
    void deleteValidGoalId() {
        //When
        when(goalDao.findById(any(UUID.class))).thenReturn(Optional.empty());

        //Then
        assertThrows(ResourceNotFoundException.class, () ->{
            service.update(new Goal().mapToDto(TestMethods.getGoal()));
        });
    }

    @Test
    @Order(8)
    @DisplayName("delete method valid userId or Password")
    void deleteValidUserIdOrPassword() {
        //When
        DeleteGoalRequest request = new DeleteGoalRequest(UUID.randomUUID(), UUID.randomUUID(), "Password");
        when(userDao.findByPrivateIdAndPassword(any(UUID.class), anyString())).thenReturn(Optional.empty());;

        //Then
        assertThrows(ResourceNotFoundException.class, () ->{
            service.delete(request);
        });
    }

    @Test
    @Order(9)
    @DisplayName("delete method valid user goal dont match")
    void deleteValidUserGoalDoesntMatch() {
        //Given
        DeleteGoalRequest request = new DeleteGoalRequest(UUID.randomUUID(), UUID.randomUUID(), "Password");

        //When
        ResponseEntity<ApiResponse> response = service.delete(request);

        //Then
        verify(userDao,times(1)).findByPrivateIdAndPassword(request.getUserPrivateId(), request.getUserPassword());
        verify(goalDao, times(1)).findById(any(UUID.class));
        assertThat(response.getBody().isSuccess()).isEqualTo(false);
        assertThat(response.getBody().getMessage()).isEqualTo("User and Goal don't match.");
    }
}