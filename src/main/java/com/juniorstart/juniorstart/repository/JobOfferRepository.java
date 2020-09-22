package com.juniorstart.juniorstart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.juniorstart.juniorstart.model.JobOffer;
import org.springframework.stereotype.Repository;

@Repository
public interface JobOfferRepository extends JpaRepository<JobOffer, Long>{
}
