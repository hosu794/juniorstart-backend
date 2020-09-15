package com.juniorstart.juniorstart.repository;


import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;


import com.juniorstart.juniorstart.model.JobOffer;

public interface JobOfferRepository extends JpaRepository<JobOffer, Long>{

}
