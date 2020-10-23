package com.juniorstart.juniorstart.service;

import com.juniorstart.juniorstart.exception.ResourceNotFoundException;
import com.juniorstart.juniorstart.exception.SaveException;
import com.juniorstart.juniorstart.model.Goal;
import com.juniorstart.juniorstart.model.User;
import com.juniorstart.juniorstart.payload.ApiResponse;
import com.juniorstart.juniorstart.payload.GoalRequestUpdate;
import com.juniorstart.juniorstart.repository.GoalDao;
import com.juniorstart.juniorstart.repository.UserDao;
import com.juniorstart.juniorstart.util.ClassMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/** Represents an Goal service.
 * @author Dawid Wit
 * @version 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GoalService {

    private final GoalDao goalDao;
    private final UserDao userDao;

    /** Create new goal for User.
     * @param email of logged user.
     * @param password of logged user.
     * @param dto of new goal.
     * @return ResponseEntity<ApiResponse> is goal had been created.
     * @throws ResourceNotFoundException if user is not found.
     * @throws SaveException if GoalDao save error.
     */
    public ResponseEntity<ApiResponse> create(String email, String password, Goal.GoalDto dto) {
        User user = findUser(email, password);
        Goal goal = ClassMapper.mapToEntity(dto);
        goal.setUser(user);

        if (isDuplicatedGoal(dto.getName(), user)){
            return ResponseEntity.ok(new ApiResponse(false,"Goal duplicate name"));
        }
        return ResponseEntity.ok(new ApiResponse(checkSave(goal),"Goal has been created"));
    }

    /** Get all goals for user.
     * @param email of logged user.
     * @param password of logged user.
     * @return ResponseEntity<List<Goal.GoalDto>> list of goals.
     * @throws ResourceNotFoundException if user is not found.
     */
    public ResponseEntity<List<Goal.GoalDto>> getGoals(String email, String password) {
        User user = findUser(email, password);
        List<Goal> goals = goalDao.findAllByUser(user);
        return ResponseEntity.ok(ClassMapper.mapToDtos(goals));
    }

    /** Update goal for current user.
     * @param request The current logged user.
     * @return ResponseEntity<ApiResponse> is goal had been updated.
     * @throws ResourceNotFoundException if user is not found.
     * @throws ResourceNotFoundException if goal is not found.
     * @throws SaveException if GoalDao save error.
     */
    public ResponseEntity<ApiResponse> update(GoalRequestUpdate request) {
        User user = findUser(request.getEmail(), request.getPassword());
        Goal previousGoal = findGoalForUser(request.getName(), user);

        Goal updatedGoal = ClassMapper.mapToEntity(request.getDto());
        updatedGoal.setId(previousGoal.getId());
        updatedGoal.setUser(user);

        return ResponseEntity.ok(new ApiResponse(checkSave(updatedGoal),"Goal has been updated"));
    }

    /** Delete goal for current user.
     * @param email The current logged user.
     * @param password The current logged user.
     * @param name of deleting goal.
     * @return ResponseEntity<ApiResponse> is goal has been deleted.
     * @throws ResourceNotFoundException if user is not found.
     * @throws ResourceNotFoundException if goal is not found.
     */
    public ResponseEntity<ApiResponse> delete(String email, String password, String name) {
        User user = findUser(email, password);
        Goal goal = findGoalForUser(name,user);

        goalDao.delete(goal);
        log.info("Goal with name :" + goal.getName() +
                "for user: " + user.getProviderId() + " has been deleted");

        return ResponseEntity.ok(new ApiResponse(true,
                "Goal with name : " + goal.getName() + " has been deleted"));
    }

    private boolean isDuplicatedGoal(String name, User user) {
        List<Goal> goals = goalDao.findAllByUser(user);
        for (Goal goal: goals) {
            if (goal.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    private User findUser(String email, String password) {
        return userDao.findByEmailAndPassword(email,password).orElseThrow(() ->
                new ResourceNotFoundException("User", "email", email));
    }

    private Goal findGoalForUser(String name, User user) {
        return goalDao.findByNameAndUser(name, user).orElseThrow(() ->
                new ResourceNotFoundException("Goal", "name", name));
    }

    private boolean checkSave(Goal goal) {
        try {
            goalDao.save(goal);
            log.info("Goal has been saved for user providerId : " + goal.getUser().getPublicId());
            return true;
        } catch (Exception e) {
            log.error("Save error of goal for user : " + goal.getUser().getPublicId(), e);
            throw new SaveException("", goal);
        }
    }
}