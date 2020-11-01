package com.juniorstart.juniorstart.service;

import com.juniorstart.juniorstart.exception.BadRequestException;
import com.juniorstart.juniorstart.exception.ResourceNotFoundException;
import com.juniorstart.juniorstart.model.EmploymentHistory;
import com.juniorstart.juniorstart.model.UserProfile;
import com.juniorstart.juniorstart.repository.UserProfileRepository;
import com.juniorstart.juniorstart.repository.EmploymentHistoryRepository;
import com.juniorstart.juniorstart.security.UserPrincipal;
import groovy.util.logging.Slf4j;
import net.bytebuddy.pool.TypePool;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class EmploymentHistoryService {

    final private EmploymentHistoryRepository employmentHistoryRepository;
    final private UserProfileRepository userProfileRepository;

    EmploymentHistoryService(EmploymentHistoryRepository employmentHistoryRepository, UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
        this.employmentHistoryRepository = employmentHistoryRepository;
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
        //foundUser.get().getEmploymentsHistory().set(employmentHistory.getId(),employmentHistory);

        foundUser.get().getEmploymentsHistory().get(employmentHistory.getId()).setCompanyName(employmentHistory.getCompanyName());
        foundUser.get().addEmploymentHistory(employmentHistory);


        return ResponseEntity.ok(userProfileRepository.save(foundUser.get()));
    }

    public ResponseEntity<?> deleteEmploymentHistory(EmploymentHistory employmentHistory, UserPrincipal currentUser) {

        Optional<UserProfile> foundUser = findUser(currentUser.getId());
//        if (foundUser.get().getEmploymentsHistory().contains(employmentHistory)) {
        System.out.println(employmentHistory.toString());
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