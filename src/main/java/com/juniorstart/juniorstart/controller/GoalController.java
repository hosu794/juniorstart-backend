package com.juniorstart.juniorstart.controller;

import com.juniorstart.juniorstart.model.Goal;
import com.juniorstart.juniorstart.payload.DeleteGoalRequest;
import com.juniorstart.juniorstart.payload.GetGoalRequest;
import com.juniorstart.juniorstart.service.GoalService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/** Represents a goal controller.
 * @author Noboseki
 * @version 1.0
 * @since 1.0
 */
@RequestMapping("goal")
@RestController
public class GoalController {

    final private GoalService service;

    public GoalController(GoalService service) {
        this.service = service;
    }

    @PostMapping("create")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> create(@Valid Goal.GoalDto dto) {
        return service.create(dto);
    }

    @GetMapping("getAll")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getAll(@Valid GetGoalRequest request) {
        return service.getAll(request);
    }

    @PutMapping("update")
    @PreAuthorize("hasRole('USER')")
    public  ResponseEntity<?> update(@Valid Goal.GoalDto dto) {
        return service.update(dto);
    }

    @DeleteMapping("delte")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> delete(@Valid DeleteGoalRequest request) {
        return service.delete(request);
    }
}
