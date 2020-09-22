package com.juniorstart.juniorstart.repository;

import com.juniorstart.juniorstart.model.JobOfferRequirements;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobOfferRequirementsRepository extends JpaRepository<JobOfferRequirements,Long> {
}
