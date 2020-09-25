package com.juniorstart.juniorstart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.juniorstart.juniorstart.payload.JobOfferRequest;
import com.juniorstart.juniorstart.service.JobOfferService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1/jobOffer")
public class JobOfferController
{
    private final JobOfferService jobOfferService;

    public JobOfferController(JobOfferService jobOfferService)
    {
        this.jobOfferService = jobOfferService;
    }
	
	@PostMapping
    @PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> addJobOffer(@Valid @RequestBody JobOfferRequest jobOfferRequest) {
        return jobOfferService.addJobOffer(jobOfferRequest);
	}

	@DeleteMapping
    @PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> deleteJobOffer(@Valid @RequestParam long publicId, @RequestParam long idJobOffer) {
        return jobOfferService.deleteJobOffer(publicId,idJobOffer);
	}
}
