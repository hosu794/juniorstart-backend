package com.juniorstart.juniorstart.service;

import java.util.List;
import java.util.stream.Collectors;
import com.juniorstart.juniorstart.exception.ResourceNotFoundException;
import com.juniorstart.juniorstart.model.*;
import com.juniorstart.juniorstart.repository.JobOfferRequirementsRepository;
import com.juniorstart.juniorstart.repository.TechnologiesRepository;
import com.juniorstart.juniorstart.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.juniorstart.juniorstart.exception.BadRequestException;
import com.juniorstart.juniorstart.payload.ApiResponse;
import com.juniorstart.juniorstart.payload.JobOfferRequest;
import com.juniorstart.juniorstart.repository.JobOfferRepository;
import com.juniorstart.juniorstart.repository.UserDao;

@Service
public class JobOfferService {
	private final JobOfferRepository jobOfferRepository;
	private final UserDao userDao;
	private final JobOfferRequirementsRepository jobOfferRequirementsRepository;
	private final TechnologiesRepository technologiesRepository;

    @Autowired
    public JobOfferService(JobOfferRepository jobOfferRepository, UserDao userDao,
				JobOfferRequirementsRepository jobOfferRequirementsRepository, TechnologiesRepository technologiesRepository) {
        this.jobOfferRepository = jobOfferRepository;
        this.userDao = userDao;
        this.jobOfferRequirementsRepository = jobOfferRequirementsRepository;
		this.technologiesRepository = technologiesRepository;
    }
    
	public ResponseEntity<?> addJobOffer(JobOfferRequest jobOfferRequest, UserPrincipal currentUser) {
		User user = userDao.findByPrivateId(currentUser.getId())
				.orElseThrow(() -> new ResourceNotFoundException("User", "privateId", currentUser.getId()));
		JobOffer jobOffer = new JobOffer();
		addTechnologies(jobOfferRequest.getTechnologies(), jobOffer);
		addRequirements(jobOfferRequest.getRequirements(), jobOffer);
		jobOffer.setMessage(jobOfferRequest.getMessage());
		jobOffer.setEmail(jobOfferRequest.getEmail());
		jobOffer.setTelephoneNumber(jobOfferRequest.getTelephoneNumber());
		jobOffer.setPosition(jobOfferRequest.getPosition());
		jobOffer.setType(jobOfferRequest.getType());
		jobOffer.setOfferCreator(user);
		jobOffer.setTechnologyType(jobOfferRequest.getTechnologyType());
		user.getJobOffers().add(jobOffer);
		jobOfferRepository.save(jobOffer);
		return ResponseEntity.ok()
			.body(jobOfferRepository.save(jobOffer));
	}

	private void addTechnologies(List<Technologies> technologiesList, JobOffer jobOffer) {
    	if(technologiesList != null)
			technologiesList.stream().filter(technologies -> technologiesRepository.findByTitle(technologies.getTitle()).isPresent())
				.map(technologies -> jobOffer.getTechnologies().add(technologiesRepository.findByTitle(technologies.getTitle()).get()))
				.collect(Collectors.toList());
	}

	private void addRequirements(List<JobOfferRequirements> requirementsList, JobOffer jobOffer) {
		if(requirementsList != null)
			requirementsList.stream().map(requirement -> jobOffer.getJobOfferRequirements()
				.add(requirement)).collect(Collectors.toList());
    }

	public ResponseEntity<?> deleteJobOffer(long idJobOffer, UserPrincipal currentUser) {
    	System.out.println(currentUser.getId());
		User user = userDao.findByPrivateId(currentUser.getId())
				.orElseThrow(() -> new ResourceNotFoundException("User", "privateId", currentUser.getId()));
		JobOffer jobOffer = jobOfferRepository.findById(idJobOffer)
				.orElseThrow(() -> new ResourceNotFoundException("JobOffer", "id", idJobOffer));
		if (!jobOffer.getOfferCreator().getPrivateId().equals(currentUser.getId()))
			throw new BadRequestException("The user does not have an offer with this id");
		user.getJobOffers().remove(jobOffer);
		jobOffer.setOfferCreator(null);
		userDao.save(user);
		jobOfferRepository.deleteById(idJobOffer);
		return ResponseEntity.ok().body(new ApiResponse(true, "Offer delleted successfully"));
	}
}
