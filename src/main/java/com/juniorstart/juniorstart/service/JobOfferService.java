package com.juniorstart.juniorstart.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.juniorstart.juniorstart.exception.ResourceNotFoundException;
import com.juniorstart.juniorstart.model.*;
import com.juniorstart.juniorstart.repository.JobOfferRequirementsRepository;
import com.juniorstart.juniorstart.repository.TechnologiesRepository;
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
    
	public ResponseEntity<?> addJobOffer(JobOfferRequest jobOfferRequest) {
		Optional<User>userOptional = Optional.ofNullable(userDao.findByPublicId(jobOfferRequest.getPublicId())
				.orElseThrow(() -> new ResourceNotFoundException("User", "publicId", jobOfferRequest.getPublicId())));
		JobOffer jobOffer = new JobOffer();
		User user = userOptional.get();
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
    	if(technologiesList != null) {
			for (int i = 0; i < technologiesList.size(); i++) {
				String nameTechnology = technologiesList.get(i).getTitle();
				Optional<Technologies> technologiesOptional = Optional.ofNullable(technologiesRepository.
						findByTitle(technologiesList.get(i).getTitle())
						.orElseThrow(() -> new ResourceNotFoundException("Technologies", "nameTechnology", nameTechnology)));
				jobOffer.getTechnologies().add(technologiesOptional.get());
			}
		}
	}

	private void addRequirements(List<JobOfferRequirements> requirement, JobOffer jobOffer) {
		if(requirement != null) {
			for(int i=0;i<requirement.size();i++) {
				JobOfferRequirements jobOfferRequirements = new JobOfferRequirements();
				jobOfferRequirements.setTextRequirement(requirement.get(i).getTextRequirement());
				jobOffer.getJobOfferRequirements().add(jobOfferRequirements);
				jobOfferRequirementsRepository.save(jobOfferRequirements);
			}
		}
	}

	public ResponseEntity<?> deleteJobOffer(long publicId, long idJobOffer) {
		Optional<User> userOptional = Optional.ofNullable(userDao.findByPublicId(publicId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "publicId", publicId)));
		User user = userOptional.get();
		Optional<JobOffer> jobOfferOptional = Optional.ofNullable(jobOfferRepository.findById(idJobOffer)
				.orElseThrow(() -> new ResourceNotFoundException("JobOffer", "id", idJobOffer)));
		JobOffer jobOffer = jobOfferOptional.get();
		if (jobOffer.getOfferCreator().getPublicId() != publicId)
			throw new BadRequestException("The user does not have an offer with this id");
		user.getJobOffers().remove(jobOffer);
		jobOffer.setOfferCreator(null);
		userDao.save(user);
		jobOfferRepository.deleteById(idJobOffer);
		return ResponseEntity.ok().body(new ApiResponse(true, "Offer delleted successfully"));
	}
}
