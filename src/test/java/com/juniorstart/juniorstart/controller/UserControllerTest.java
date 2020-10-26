package com.juniorstart.juniorstart.controller;

import com.google.gson.Gson;
import com.juniorstart.juniorstart.exception.ResourceNotFoundException;
import com.juniorstart.juniorstart.model.audit.UserStatus;
import com.juniorstart.juniorstart.payload.ApiResponse;
import com.juniorstart.juniorstart.payload.ChangeMailRequest;
import com.juniorstart.juniorstart.payload.ChangePasswordRequest;
import com.juniorstart.juniorstart.payload.ChangeStatusRequest;
import com.juniorstart.juniorstart.service.UserService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/** Test for UserController class.
 * @author Noboseki
 * @version 1.0
 * @since 1.0
 */
@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserControllerTest {
    @Mock
    UserService userService;
    @InjectMocks
    UserController userController;

    Gson gson = new Gson();
    MockMvc mockMvc;

    ResponseEntity<ApiResponse> response = ResponseEntity.ok((new ApiResponse(true,"Test Ok")));
    ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest("Password 2",UUID.randomUUID(), "Password");
    ChangeMailRequest changeMailRequest = new ChangeMailRequest("test@tes.com", UUID.randomUUID(), "Password");
    ChangeStatusRequest changeStatusRequest = new ChangeStatusRequest(UserStatus.OPEN, UUID.randomUUID(), "Password");

    String changePasswordJson = gson.toJson(changePasswordRequest);
    String changeEmailJson = gson.toJson(changeMailRequest);
    String changeStatusJson = gson.toJson(changeStatusRequest);

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    @Order(1)
    @DisplayName("Change password Correct")
    void testChangePasswordCorrect() throws Exception {
        //When
        when(userService.changePassword(any(ChangePasswordRequest.class))).thenReturn(response);

        //Then
        correctMvcPerform("/api/changePassword", changePasswordJson);
        verify(userService, times(1)).changePassword(any(ChangePasswordRequest.class));
    }

    @Test
    @Order(2)
    @DisplayName("Change password Valid")
    void testChangePasswordValid() throws Exception {
        //When
        when(userService.changePassword(any(ChangePasswordRequest.class))).thenThrow(ResourceNotFoundException.class);

        //Then
        validMvcPerform("/api/changePassword", changePasswordJson);
        verify(userService, times(1)).changePassword(any(ChangePasswordRequest.class));
    }

    @Test
    @Order(3)
    @DisplayName("Change mail Correct")
    void testChangeMailCorrect() throws Exception {
        //When
        when(userService.changeEmail(any(ChangeMailRequest.class))).thenReturn(response);

        //Then
        correctMvcPerform("/api/changeMail", changeEmailJson);
        verify(userService, times(1)).changeEmail(any(ChangeMailRequest.class));
    }

    @Test
    @Order(4)
    @DisplayName("Change mail Valid")
    void testChangeMailValid() throws Exception {
        //When
        when(userService.changeEmail(any(ChangeMailRequest.class))).thenThrow(ResourceNotFoundException.class);

        //Then
        validMvcPerform("/api/changeMail", changeEmailJson);
        verify(userService, times(1)).changeEmail(any(ChangeMailRequest.class));
    }

    @Test
    @Order(5)
    @DisplayName("Change status Correct")
    void testChangeStatusCorrect() throws Exception {
        //When
        when(userService.changeStatus(any(ChangeStatusRequest.class))).thenReturn(response);

        //Then
        correctMvcPerform("/api/changeStatus", changeStatusJson);
        verify(userService, times(1)).changeStatus(any(ChangeStatusRequest.class));
    }

    @Test
    @Order(6)
    @DisplayName("Change status Valid")
    void testChangeStatusValid() throws Exception {
        //When
        when(userService.changeStatus(any(ChangeStatusRequest.class))).thenThrow(ResourceNotFoundException.class);

        //Then
        validMvcPerform("/api/changeStatus", changeStatusJson);
        verify(userService, times(1)).changeStatus(any(ChangeStatusRequest.class));
    }

    @Test
    @Order(7)
    @DisplayName("Get status list")
    void testGetStatusList() throws Exception {
        List<String> statuses = Stream
                .of(UserStatus.values())
                .map(UserStatus::name)
                .collect(Collectors.toList());

        when(userService.getStatusList()).thenReturn(ResponseEntity.ok(statuses));

        mockMvc.perform(get("/api/statusList"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(4)))
                .andExpect(jsonPath("$.[0]",is("LOOKING_FOR_A_JOB")))
                .andExpect(jsonPath("$.[1]",is("HIRED")))
                .andExpect(jsonPath("$.[2]",is("FREELANCER")))
                .andExpect(jsonPath("$.[3]",is("OPEN"))).andReturn();

        verify(userService,times(1)).getStatusList();
    }

    /** method for setup correct test by mockMvc.perform.
     * @param urlPath of testing endpoint.
     * @param jsonObject for endpoint created by Gson.class
     * @return MvcResult of testing endpoint.
     */
    private MvcResult correctMvcPerform(String urlPath, String jsonObject) throws Exception {
        return mockMvc.perform(post(urlPath)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding("UTF-8")
                    .content(jsonObject))
                .andExpect(status().isOk())
                .andExpect(jsonPath("success",is(true)))
                .andExpect(jsonPath("message",is("Test Ok"))).andReturn();
    }

    /** method for setup valid test by mockMvc.perform.
     * @param urlPath of testing endpoint.
     * @param jsonObject for endpoint created by Gson.class
     * @return MvcResult of testing endpoint.
     */
    private MvcResult validMvcPerform(String urlPath, String jsonObject) throws Exception {
        return mockMvc.perform(post(urlPath)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding("UTF-8")
                    .content(jsonObject))
                .andExpect(status().is4xxClientError()).andReturn();
    }
}