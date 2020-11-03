package com.juniorstart.juniorstart.service;

import com.juniorstart.juniorstart.exception.BadRequestException;
import com.juniorstart.juniorstart.exception.ResourceNotFoundException;
import com.juniorstart.juniorstart.model.EmploymentHistory;
import com.juniorstart.juniorstart.model.UserProfile;
import com.juniorstart.juniorstart.repository.UserProfileRepository;
import com.juniorstart.juniorstart.repository.EmploymentHistoryRepository;
import com.juniorstart.juniorstart.security.UserPrincipal;
import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
@Service
public class EmploymentHistoryService {

    private final EmploymentHistoryRepository employmentHistoryRepository;
    private final UserProfileRepository userProfileRepository;

    public ResponseEntity<?> getEmploymentHistory(UUID id, UserPrincipal currentUser) {

        findUser(currentUser.getId());

        return ResponseEntity.ok(userProfileRepository.findByPrivateId(id));
    }

    public ResponseEntity<?> addEmploymentHistory(EmploymentHistory employmentHistory, UserPrincipal currentUser) {

        Optional<UserProfile> foundUser = findUser(currentUser.getId());
        checkIsCurrentEmployer(employmentHistory);

        foundUser.get().addEmploymentHistory(employmentHistory);

        return ResponseEntity.ok(userProfileRepository.save(foundUser.get()));
    }

    public ResponseEntity<?> updateEmploymentHistory(EmploymentHistory employmentHistory, UserPrincipal currentUser) {

        Optional<UserProfile> foundUser = findUser(currentUser.getId());
        checkIsCurrentEmployer(employmentHistory);

        foundUser.get().getEmploymentsHistory().set(employmentHistory.getId(), employmentHistory);

        foundUser.get().addEmploymentHistory(employmentHistory);

        return ResponseEntity.ok(userProfileRepository.save(foundUser.get()));
    }

    public ResponseEntity<?> deleteEmploymentHistory(EmploymentHistory employmentHistory, UserPrincipal currentUser) {

        Optional<UserProfile> foundUser = findUser(currentUser.getId());

        if (foundUser.get().getEmploymentsHistory().contains(employmentHistory)) {
        employmentHistoryRepository.deleteById((long) employmentHistory.getId());
        return ResponseEntity.ok().build();
        } else {
            throw new ResourceNotFoundException("EmploymentHistory", "ID", employmentHistory.getId());
        }
    }

    public Optional<UserProfile> findUser(UUID id) {
        return Optional.ofNullable(userProfileRepository.findByPrivateId(id).orElseThrow(()->
                new ResourceNotFoundException("UserProfile", "ID", id)));
    }

    public void checkIsCurrentEmployer(EmploymentHistory employmentHistory) {
        if (employmentHistory.getIsCurrentEmployment()) {
            if (employmentHistory.getDateEndOfEmployment() != null) {
                throw new BadRequestException("You can't fill end date at current employer");
            }
        }
    }
}