package com.juniorstart.juniorstart.repository;

import com.juniorstart.juniorstart.model.EmploymentHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmploymentHistoryRepository extends JpaRepository <EmploymentHistory, Long> {
}
