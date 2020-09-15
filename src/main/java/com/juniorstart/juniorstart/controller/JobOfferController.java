package com.juniorstart.juniorstart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.juniorstart.juniorstart.payload.JobOfferRequest;
import com.juniorstart.juniorstart.service.JobOfferService;
import jakarta.validation.Valid;

@RestController
public class JobOfferController
{
	JobOfferService jobOfferService;

    @Autowired
    public JobOfferController(JobOfferService jobOfferService)
    {
        this.jobOfferService = jobOfferService;
    }
	
	@PostMapping("/addJobOffer")
	public ResponseEntity<?> addJobOffer(@Valid @RequestBody JobOfferRequest jobOfferRequest)
	{
        return jobOfferService.addJobOffer(jobOfferRequest);
	}
	
	@DeleteMapping("/deleteJobOffer")
	public ResponseEntity<?> deleteJobOffer(@Valid @RequestParam long publicId)
	{
        return jobOfferService.deleteJobOffer(publicId);
	}
}
