package com.juniorstart.juniorstart.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.juniorstart.juniorstart.exception.ResourceNotFoundException;
import com.juniorstart.juniorstart.model.JobOfferRequirements;
import com.juniorstart.juniorstart.model.Technologies;
import com.juniorstart.juniorstart.repository.JobOfferRequirementsRepository;
import com.juniorstart.juniorstart.repository.TechnologiesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.juniorstart.juniorstart.exception.BadRequestException;
import com.juniorstart.juniorstart.model.JobOffer;
import com.juniorstart.juniorstart.model.User;
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
		//Set<JobOfferRequirements> jobOfferRequirementsSet= (Set<JobOfferRequirements>) jobOfferRequest.getRequirements().stream().map(w ->w);
		//System.out.println(jobOfferRequirementsSet.size());
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
		user.addJobOffer(jobOffer);
		jobOfferRepository.save(jobOffer);
		return ResponseEntity.ok()
			.body(jobOfferRepository.save(jobOffer));

	}

	private void addTechnologies(List<Technologies> technologiesList, JobOffer jobOffer) {
    	for(int i=0;i<technologiesList.size();i++) {
    		String nameTechnology = technologiesList.get(i).getNameTechnology();
			Optional<Technologies> technologiesOptional = Optional.ofNullable(technologiesRepository.
					findByNameTechnology(technologiesList.get(i).getNameTechnology())
					.orElseThrow(() -> new ResourceNotFoundException("Technologies", "nameTechnology", nameTechnology)));
			jobOffer.addTechnologies(technologiesOptional.get());
		}
	}

	private void addRequirements(List<JobOfferRequirements> requirement, JobOffer jobOffer) {
		for(int i=0;i<requirement.size();i++) {
			JobOfferRequirements jobOfferRequirements = new JobOfferRequirements();
			jobOfferRequirements.setTextRequirement(requirement.get(i).getTextRequirement());
			jobOffer.addJobRequirements(jobOfferRequirements);
			jobOfferRequirementsRepository.save(jobOfferRequirements);
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
		user.deleteJobOffer(jobOffer);
		jobOffer.setOfferCreator(null);
		userDao.save(user);
		jobOfferRepository.deleteById(idJobOffer);
		return ResponseEntity.ok().body(new ApiResponse(true, "Offer delleted successfully"));
	}
}
