package com.juniorstart.juniorstart.service;

import java.util.*;
import com.juniorstart.juniorstart.model.JobOffer;
import com.juniorstart.juniorstart.repository.TechnologiesRepository;
import com.juniorstart.juniorstart.security.UserPrincipal;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import com.juniorstart.juniorstart.model.AuthProvider;
import com.juniorstart.juniorstart.model.User;
import com.juniorstart.juniorstart.payload.JobOfferRequest;
import com.juniorstart.juniorstart.repository.JobOfferRepository;
import com.juniorstart.juniorstart.repository.UserDao;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@RunWith(MockitoJUnitRunner.class)
public class JobOfferServiceTest {
	UserDao userDao = Mockito.mock(UserDao.class);
	JobOfferRepository jobOfferRepository = Mockito.mock(JobOfferRepository.class);
	TechnologiesRepository technologiesRepository = Mockito.mock(TechnologiesRepository.class);
	JobOfferService jobOfferService = new JobOfferService(jobOfferRepository, userDao, technologiesRepository);

	User user;
	JobOfferRequest jobOfferRequest;
	UUID uuid = UUID.randomUUID();
	JobOffer jobOffer;
	UserPrincipal userPrincipal;
	
	@Before
	public void initialize() throws Exception {
		jobOfferRequest = JobOfferRequest.builder().message("Zatrudnie Cie jesli chcesz").type("ogloszenie o prace").position("junior").build();
		jobOffer = JobOffer.builder().id(11L).message(jobOfferRequest.getMessage()).type(jobOfferRequest.getType()).position(jobOfferRequest.getPosition()).build();
		Set<JobOffer> setJobsOffer = new HashSet<>();
		setJobsOffer.add(jobOffer);
		user = User.builder().name("MockName").password("Password").publicId(12L).provider(AuthProvider.local).email("grzesiek12@gmail.com").emailVerified(true).privateId(uuid).jobOffers(setJobsOffer).build();
		userPrincipal = UserPrincipal.create(user);
		jobOffer.setOfferCreator(user);
	}
	
	@Test
	public void should_addJobOffer() throws Exception {
		Mockito.when(userDao.findByPrivateId(ArgumentMatchers.any(UUID.class))).thenReturn(Optional.of(user));
		Mockito.when(jobOfferRepository.save(ArgumentMatchers.any(JobOffer.class))).thenReturn(jobOffer);
		Assert.assertEquals(jobOfferService.addJobOffer(jobOfferRequest, userPrincipal),jobOffer);
	}

	@Test
	public void should_deleteJobOffer() throws Exception {
		Mockito.when(userDao.findByPrivateId(ArgumentMatchers.any())).thenReturn(Optional.of(user));
		Mockito.when(jobOfferRepository.findById(ArgumentMatchers.any())).thenReturn(Optional.of(jobOffer));
		Mockito.when(userDao.save(ArgumentMatchers.any(User.class))).thenReturn(user);
		Assert.assertEquals(jobOfferService.deleteJobOffer(11L, userPrincipal).getStatusCode(), HttpStatus.OK);
	}
}
