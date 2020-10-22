package com.juniorstart.juniorstart.controller;

import com.juniorstart.juniorstart.model.JobOffer;
import com.juniorstart.juniorstart.security.annotation.CurrentUser;
import com.juniorstart.juniorstart.security.UserPrincipal;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.juniorstart.juniorstart.payload.JobOfferRequest;
import com.juniorstart.juniorstart.service.JobOfferService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1/jobOffer")
public class JobOfferController {
	private final JobOfferService jobOfferService;

	public JobOfferController(JobOfferService jobOfferService) {
		this.jobOfferService = jobOfferService;
	}
	
	@PostMapping
	@PreAuthorize("hasRole('USER')")
	public JobOffer addJobOffer(@CurrentUser UserPrincipal currentUser, @Valid @RequestBody JobOfferRequest jobOfferRequest) {
		return jobOfferService.addJobOffer(jobOfferRequest, currentUser);
	}

	@PreAuthorize("hasRole('USER')")
	@DeleteMapping
	public ResponseEntity<?> deleteJobOffer(@CurrentUser UserPrincipal currentUser, @Valid @RequestParam long idJobOffer) {
		return jobOfferService.deleteJobOffer(idJobOffer, currentUser);
	}
}
