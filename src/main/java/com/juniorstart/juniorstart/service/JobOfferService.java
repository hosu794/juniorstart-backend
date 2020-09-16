package com.juniorstart.juniorstart.service;

import java.util.Optional;

import com.juniorstart.juniorstart.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.juniorstart.juniorstart.exception.BadRequestException;
import com.juniorstart.juniorstart.model.JobOffer;
import com.juniorstart.juniorstart.model.User;
import com.juniorstart.juniorstart.payload.ApiResponse;
import com.juniorstart.juniorstart.payload.JobOfferRequest;
import com.juniorstart.juniorstart.repository.JobOfferRepository;
import com.juniorstart.juniorstart.repository.UserDao;

@Service
public class JobOfferService
{

	JobOfferRepository jobOfferRepository;
	UserDao userDao;

    @Autowired
    public JobOfferService(JobOfferRepository jobOfferRepository, UserDao userDao)
    {
        this.jobOfferRepository = jobOfferRepository;
        this.userDao=userDao;
    }
    
	public ResponseEntity<?> addJobOffer(JobOfferRequest jobOfferRequest)
	{
		Optional<User>userOptional= Optional.ofNullable(userDao.findByPublicId(jobOfferRequest.getPublicId())
				.orElseThrow(() -> new ResourceNotFoundException("User", "publicId", jobOfferRequest.getPublicId())));

			
		User user = userOptional.get();

		JobOffer jobOffer=new JobOffer();
		jobOffer.setMessage(jobOfferRequest.getMessage());
		jobOffer.setContact(jobOfferRequest.getContact());
		jobOffer.setPosition(jobOfferRequest.getPosition());
		jobOffer.setRequirements(jobOfferRequest.getRequirements());
		jobOffer.setTechnologies(jobOfferRequest.getTechnologies());
		jobOffer.setType(jobOfferRequest.getType());
		jobOffer.setOfferCreator(user);
		user.addJobOffer(jobOffer);
		userDao.save(user);
		jobOfferRepository.save(jobOffer);
				
		return ResponseEntity.ok()
			.body(new ApiResponse(true, "Offer added successfully"));
	}



	public ResponseEntity<?> deleteJobOffer(long publicId, long idJobOffer)
	{
		
		Optional<User>userOptional= Optional.ofNullable(userDao.findByPublicId(publicId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "publicId", publicId)));
		User user = userOptional.get();

		Optional<JobOffer>jobOfferOptional= Optional.ofNullable(jobOfferRepository.findById(idJobOffer)
				.orElseThrow(() -> new ResourceNotFoundException("JobOffer", "id", idJobOffer)));
		JobOffer jobOffer=jobOfferOptional.get();

        if(jobOffer.getOfferCreator().getPublicId()!=publicId)
            throw new BadRequestException ("The user does not have an offer with this id ");

        user.deleteJobOffer(jobOffer);
        jobOfferRepository.deleteById(jobOffer.getId());
				
        userDao.save(user);
	
        return ResponseEntity.ok().body(new ApiResponse(true, "Offer delleted successfully"));


	}
}
