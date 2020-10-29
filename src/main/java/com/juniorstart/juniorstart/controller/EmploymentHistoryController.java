package com.juniorstart.juniorstart.controller;


import com.juniorstart.juniorstart.model.EmploymentHistory;
import com.juniorstart.juniorstart.security.CurrentUser;
import com.juniorstart.juniorstart.security.UserPrincipal;
import com.juniorstart.juniorstart.service.EmploymentHistoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user/employmenthistory")
public class EmploymentHistoryController {

    private EmploymentHistoryService employmentHistoryService;

    public EmploymentHistoryController(EmploymentHistoryService employmentHistoryService) {
        this.employmentHistoryService = employmentHistoryService;
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("")
    public ResponseEntity<?> addUserProfileTechnology(@RequestBody EmploymentHistory employmentHistory, @CurrentUser UserPrincipal userPrincipal){
        return employmentHistoryService.addEmploymentHistory(employmentHistory, userPrincipal);
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("")
    public ResponseEntity<?> updateUserProfileTechnology(@RequestBody EmploymentHistory employmentHistory, @CurrentUser UserPrincipal userPrincipal){
        return employmentHistoryService.updateEmploymentHistory(employmentHistory, userPrincipal);
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("")
    public ResponseEntity<?> deleteUserProfileTechnology(@RequestBody EmploymentHistory employmentHistory, @CurrentUser UserPrincipal userPrincipal){
        return employmentHistoryService.deleteEmploymentHistory(employmentHistory, userPrincipal);
    }

}
