package com.juniorstart.juniorstart.service;


import java.util.Optional;
import java.util.UUID;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import com.juniorstart.juniorstart.model.AuthProvider;
import com.juniorstart.juniorstart.model.User;
import com.juniorstart.juniorstart.payload.JobOfferRequest;
import com.juniorstart.juniorstart.repository.JobOfferRepository;
import com.juniorstart.juniorstart.repository.UserDao;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(MockitoJUnitRunner.class)
public class JobOfferSericeTest {
	
	UserDao userDao = Mockito.mock(UserDao.class);
	JobOfferRepository jobOfferRepository = Mockito.mock(JobOfferRepository.class);
	
	JobOfferService jobOfferService;
	User user;
	JobOfferRequest jobOfferRequest;
	UUID uuid = UUID.randomUUID();
	
	@Before
    public void initialize() throws Exception
	{
        user = User.builder().name("MockName").password("Password").publicId(12L).provider(AuthProvider.local).email("mail@mail.com").emailVerified(true).privateId(uuid).build();
        jobOfferRequest = jobOfferRequest.builder().publicId(user.getPublicId()).message("Zatrudnie Cie jesli chcesz").type("ogloszenie o prace").position("junior").technologies("java").requirements("2 lata doswiadczenia").contact("654 782 632").build();
    }
	
	 @Test
	 public void should_addJobOffer() throws Exception
	 {
	     Mockito.when(userDao.findByPublicId(ArgumentMatchers.any(Long.class))).thenReturn(Optional.of(user));
	     Mockito.when(userDao.save(ArgumentMatchers.any(User.class))).thenReturn(user);
	     Assert.assertEquals(jobOfferService.addJobOffer(jobOfferRequest).getStatusCode(), HttpStatus.OK);
	 }
	 
	 @Test
	 public void should_deleteJobOffer() throws Exception
	 {
	    Mockito.when(userDao.findByPublicId(ArgumentMatchers.any(Long.class))).thenReturn(Optional.of(user));
	    Mockito.when(userDao.save(ArgumentMatchers.any(User.class))).thenReturn(user);
	    Assert.assertEquals(jobOfferService.addJobOffer(jobOfferRequest).getStatusCode(), HttpStatus.OK);
	 }
}
