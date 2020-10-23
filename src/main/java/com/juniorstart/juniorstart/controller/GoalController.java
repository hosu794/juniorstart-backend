package com.juniorstart.juniorstart.controller;

import com.juniorstart.juniorstart.model.Goal;
import com.juniorstart.juniorstart.model.User;
import com.juniorstart.juniorstart.payload.GoalRequestUpdate;
import com.juniorstart.juniorstart.security.annotation.CurrentUser;
import com.juniorstart.juniorstart.security.annotation.UserRole;
import com.juniorstart.juniorstart.service.GoalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/** Represents an goal service.
 * @author Dawid Wit
 * @version 1.0
 * @since 1.0
 */
@RestController("goal")
@RequiredArgsConstructor
public class GoalController {

    private final GoalService service;

    @GetMapping("getAll")
    @UserRole
    public ResponseEntity<?> getAll(@CurrentUser User user) {
        return service.getGoals(user.getEmail(), user.getPassword());
    }

    @PostMapping("create")
    @UserRole
    public ResponseEntity<?> create(@CurrentUser User user, @RequestBody @Valid Goal.GoalDto dto) {
        return service.create(user.getEmail(), user.getPassword(), dto);
    }

    @PutMapping("update/{name}")
    @UserRole
    public ResponseEntity<?> update(@CurrentUser User user,
                                    @RequestBody @Valid Goal.GoalDto dto,
                                    @PathVariable String name) {
        GoalRequestUpdate update = new GoalRequestUpdate(user.getEmail(), user.getPassword(), dto, name);
        return service.update(update);
    }

    @DeleteMapping("delete/{name}")
    @UserRole
    public ResponseEntity<?> delete (@CurrentUser User user, @PathVariable String name) {
        return service.delete(user.getEmail(),user.getPassword(),name);
    }
}
