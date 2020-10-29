package com.juniorstart.juniorstart.service;

import com.juniorstart.juniorstart.model.EmploymentHistory;
import com.juniorstart.juniorstart.repository.EmploymentHistoryRepository;
import com.juniorstart.juniorstart.security.UserPrincipal;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class EmploymentHistoryService {


    private EmploymentHistoryRepository employmentHistoryRepository;

    EmploymentHistoryService(EmploymentHistoryRepository employmentHistoryRepository) {
        this.employmentHistoryRepository = employmentHistoryRepository;
    }


    public ResponseEntity<?> addEmploymentHistory(EmploymentHistory employmentHistory, UserPrincipal currentUser) {



        return ResponseEntity.ok().build();

    }

    public ResponseEntity<?> updateEmploymentHistory(EmploymentHistory employmentHistory, UserPrincipal currentUser) {




        return ResponseEntity.ok().build();

    }

    public ResponseEntity<?> deleteEmploymentHistory(EmploymentHistory employmentHistory, UserPrincipal currentUser) {


        return ResponseEntity.ok().build();
    }



}
