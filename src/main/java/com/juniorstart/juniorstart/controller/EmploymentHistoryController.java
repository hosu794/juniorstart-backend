package com.juniorstart.juniorstart.controller;

import com.juniorstart.juniorstart.model.EmploymentHistory;
import com.juniorstart.juniorstart.security.CurrentUser;
import com.juniorstart.juniorstart.security.UserPrincipal;
import com.juniorstart.juniorstart.service.EmploymentHistoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/user/employmenthistory")
public class EmploymentHistoryController {

    private final EmploymentHistoryService employmentHistoryService;

    public EmploymentHistoryController(EmploymentHistoryService employmentHistoryService) {
        this.employmentHistoryService = employmentHistoryService;
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("")
    public ResponseEntity<?> getEmploymentHistory(@RequestBody UUID id, @CurrentUser UserPrincipal userPrincipal){
        return employmentHistoryService.getEmploymentHistory(id, userPrincipal);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("")
    public ResponseEntity<?> addEmploymentHistory(@RequestBody EmploymentHistory employmentHistory, @CurrentUser UserPrincipal userPrincipal){
        return employmentHistoryService.addEmploymentHistory(employmentHistory, userPrincipal);
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("")
    public ResponseEntity<?> updateEmploymentHistory(@RequestBody EmploymentHistory employmentHistory, @CurrentUser UserPrincipal userPrincipal){
        return employmentHistoryService.updateEmploymentHistory(employmentHistory, userPrincipal);
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("")
    public ResponseEntity<?> deleteEmploymentHistory(@RequestBody EmploymentHistory employmentHistory, @CurrentUser UserPrincipal userPrincipal){
        return employmentHistoryService.deleteEmploymentHistory(employmentHistory, userPrincipal);
    }

}
