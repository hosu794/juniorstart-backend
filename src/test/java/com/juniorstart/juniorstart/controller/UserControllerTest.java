package com.juniorstart.juniorstart.controller;

import com.juniorstart.juniorstart.model.AuthProvider;
import com.juniorstart.juniorstart.model.User;
import com.juniorstart.juniorstart.model.audit.UserStatus;
import com.juniorstart.juniorstart.payload.ChangeMailRequest;
import com.juniorstart.juniorstart.payload.ChangePasswordRequest;
import com.juniorstart.juniorstart.payload.ChangeStatusRequest;
import com.juniorstart.juniorstart.repository.UserDao;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithUserDetails;
import java.util.UUID;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/** Test for UserController class.
 * @author Noboseki
 * @version 1.0
 * @since 1.0
 */
class UserControllerTest extends ControllerIntegrationTest {

    private final String BASIC_URL = "/api";

    User user;
    ChangeMailRequest changeMailRequest;
    ChangePasswordRequest changePasswordRequest;
    ChangeStatusRequest changeStatusRequest;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserDao userDao;

    @Override
    @BeforeEach
    void setUp() throws Exception {

        super.setUp();

        user = User.builder()
                .name("name")
                .password("password")
                .email("some@example.com")
                .provider(AuthProvider.local)
                .password("password")
                .emailVerified(false)
                .userStatus(UserStatus.LOOKING_FOR_A_JOB)
                .build();

        userDao.save(user);


        UUID privateIdForUser = userDao.findByEmail(user.getEmail()).get().getPrivateId();

        changeMailRequest = ChangeMailRequest.builder()
                .email("new@example.com")
                .privateId(privateIdForUser)
                .password(user.getPassword())
                .build();

       changePasswordRequest = ChangePasswordRequest.builder().privateId(privateIdForUser).Password("Some Password").newPassword("New Password").build();

       changeStatusRequest = new ChangeStatusRequest(UserStatus.OPEN, privateIdForUser, user.getPassword());

    }

    @Test
    @WithUserDetails("someEmail")
    public void getCurrentUser() throws Exception {
        mockMvc.perform(get(BASIC_URL + "/user/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email",is("someEmail")))
                .andExpect(jsonPath("$.name",is("Somename")))
                .andExpect(jsonPath("$.id",is(12)));
    }

    @Test
    @WithUserDetails("someEmail")
    public void changeEmail() throws Exception {
        String jsonRequest = gson.toJson(changeMailRequest);

        mockMvc.perform(post(BASIC_URL + "/changeMail")
                        .characterEncoding("UTF-8")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.success", is(true)))
                        .andExpect(jsonPath("$.message", is("Email has change")));

    }

    @Test
    @WithUserDetails("someEmail")
    public void changePassword() throws Exception {

      String jsonRequest = gson.toJson(changePasswordRequest);

      mockMvc.perform(post(  "/api/changePassword")
              .contentType(MediaType.APPLICATION_JSON)
              .content(jsonRequest)
              .characterEncoding("UTF-8"))
              .andExpect(status().isOk())
              .andExpect(jsonPath("$.success", is(true)));

    }

    @Test
    @WithUserDetails("someEmail")
    public void changeStatus() throws Exception {

        String jsonRequest = gson.toJson(changeStatusRequest);

        mockMvc.perform(post(BASIC_URL + "/changeStatus")
                .content(jsonRequest)
                .characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is(" Status has change")));
    }

    @Test
    @WithUserDetails("someEmail")
    public void getStatusList() throws Exception {
        mockMvc.perform(get(BASIC_URL + "/statusList"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0]", is("LOOKING_FOR_A_JOB")))
                .andExpect(jsonPath("$.[1]", is("HIRED")))
                .andExpect(jsonPath("$.[2]", is("FREELANCER")))
                .andExpect(jsonPath("$.[3]", is("OPEN")));
    }

    @Test
    @WithUserDetails("someEmail")
    public void getAllUserSummaries() throws Exception {
        mockMvc.perform(get(BASIC_URL + "/users/summaries"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(user.getPublicId())))
                .andExpect(jsonPath("$.[0].name", is(user.getName())))
                .andExpect(jsonPath("$.[0].email", is(user.getEmail())));
    }

}