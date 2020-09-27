package com.juniorstart.juniorstart.service;

import com.juniorstart.juniorstart.exception.ResourceNotFoundException;
import com.juniorstart.juniorstart.model.Goal;
import com.juniorstart.juniorstart.model.User;
import com.juniorstart.juniorstart.payload.ApiResponse;
import com.juniorstart.juniorstart.payload.DeleteGoalRequest;
import com.juniorstart.juniorstart.payload.GetGoalRequest;
import com.juniorstart.juniorstart.payload.interfaces.InterfaceChangeRequest;
import com.juniorstart.juniorstart.repository.GoalDao;
import com.juniorstart.juniorstart.repository.UserDao;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

/** Represents an goal service 26-09-2020.
 * @author Noboseki
 * @version 1.0
 * @since 1.0
 */
@Service
@Slf4j
public class GoalService {
    private GoalDao goalDao;
    private UserDao userDao;

    public GoalService(GoalDao goalDao, UserDao userDao) {
        this.goalDao = goalDao;
        this.userDao = userDao;
    }

    /** Create response entity.
     * @param dto of creating goal.
     * @return the apiResponse is goal has been created.
     */
    public ResponseEntity<ApiResponse> create(@Valid Goal.GoalDto dto) {
        return saveGoal(dto, "created");
    }

    /** Gets all goals of user.
     * @param request class for get all goals request with userId and userPassword.
     * @return Set of user goals.
     */
    public ResponseEntity<Set<Goal.GoalDto>> getAll(@Valid  GetGoalRequest request) {
        User user = getUserFromDb(request);

        Set<Goal> userGoals = goalDao.findAllByUser(user);
        log.info("Goals for user has been taken from DB.");
        Set<Goal.GoalDto> userDtos = new Goal().mapToDtos(userGoals);
        return ResponseEntity.ok(userDtos);
    }

    /** Update response entity.
     * @param dto the updating goal.
     * @return the response entity
     */
    public ResponseEntity<ApiResponse> update(@Valid Goal.GoalDto dto){
        getGoalFromDb(dto.getPrivateId());
        return saveGoal(dto,"updated");
    }

    /** Delete the entity.
     * @param request class for get all goals request with userId and userPassword and goalId.
     * @return the apiResponse is goal has been deleted.
     */
    public ResponseEntity<ApiResponse> delete(@Valid DeleteGoalRequest request) {
        User user = getUserFromDb(request);
        Goal goal = getGoalFromDb(request.getGoalId());

        if (user.equals(goal.getUser())){
            log.info("User and Goal match.");
            goalDao.delete(goal);
            log.info("Goal has been deleted");
            return ResponseEntity.ok(new ApiResponse(true,"Goal has been deleted"));
        } else {
            return ResponseEntity.badRequest().body(new ApiResponse(false,"User and Goal don't match."));
        }
    }

    /** Refactored code for getting user from Database.
     * @param changeRequest is interface common for all change user data requests.
     * @return User from repository to change chosen data.
     * @throws ResourceNotFoundException cannot find user by name and password.
     */
    private User getUserFromDb(InterfaceChangeRequest changeRequest) {
        User user = userDao.findByPrivateIdAndPassword(changeRequest.getUserPrivateId(), changeRequest.getUserPassword())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", changeRequest.getUserPrivateId()));
        log.info("User has been checked and taken.");
        return user;
    }

    /** Refactored code for getting goal from Database.
     * @param goalId of looking goal.
     * @return User from repository to change chosen data.
     * @throws ResourceNotFoundException cannot find goal by id.
     */
    private Goal getGoalFromDb(UUID goalId) {
        Goal goal = goalDao.findById(goalId)
                .orElseThrow(() -> new ResourceNotFoundException("Goal", "id", goalId));
        log.info("Goal has been checked and taken.");
        return goal;
    }

    /** Refactored code for getting user from Database.
     * @param dto of saving goal.
     * @param actionName of making action.
     * @return User from repository to change chosen data.
     * @throws RuntimeException "Goal save error.".
     */
    private ResponseEntity<ApiResponse> saveGoal(Goal.GoalDto dto, String actionName) {
            goalDao.save(new Goal().mapToEntity(dto));
            log.info("Goal has been " + actionName + ".");
            return ResponseEntity.ok(new ApiResponse(true,"Goal has been " + actionName + "."));
    }
}