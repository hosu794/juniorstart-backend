package com.juniorstart.juniorstart.service;

import java.util.Optional;

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
		Optional<User>userOptional=userDao.findByPublicId(jobOfferRequest.getPublicId());
		if(userOptional.isEmpty())
			throw new BadRequestException("The user of that ID is not found");
			
		User user = userOptional.get();
				
		JobOffer jobOffer=new JobOffer();
		jobOffer.setMessage(jobOfferRequest.getMessage());
		jobOffer.setContact(jobOfferRequest.getContact());
		jobOffer.setPosition(jobOfferRequest.getPosition());
		jobOffer.setRequirements(jobOfferRequest.getRequirements());
		jobOffer.setTechnologies(jobOfferRequest.getTechnologies());
		jobOffer.setType(jobOfferRequest.getType());
				
		user.setJobOffer(jobOffer);
		jobOffer.setUser(user);
				
		return ResponseEntity.ok()
			.body(userDao.save(user).getJobOffer());
	}

	public ResponseEntity<?> deleteJobOffer(long publicId)
	{
		
		Optional<User>userOptional=userDao.findByPublicId(publicId);
		if(userOptional.isEmpty())
			throw new BadRequestException ("The user of that ID is not found");
		
		User user = userOptional.get();
		JobOffer jobOffer=user.getJobOffer();
			
		if(jobOffer!=null)
		{
			user.setJobOffer(null);
			jobOfferRepository.deleteById(jobOffer.getId());
				
			userDao.save(user);
	
			return ResponseEntity.ok()
				.body(new ApiResponse(true, "Offer delleted successfully"));
		}
		else 
			throw new BadRequestException("offer does not exist");			
	} 
}
