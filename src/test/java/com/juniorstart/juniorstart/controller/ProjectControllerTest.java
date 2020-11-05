package com.juniorstart.juniorstart.controller;

import com.juniorstart.juniorstart.model.*;
import com.juniorstart.juniorstart.model.audit.UserStatus;
import com.juniorstart.juniorstart.payload.ProjectRequest;
import com.juniorstart.juniorstart.payload.UserSummary;
import com.juniorstart.juniorstart.security.UserPrincipal;
import com.juniorstart.juniorstart.util.MockUtil;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.test.context.support.WithUserDetails;


import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProjectControllerTest extends ControllerIntegrationTest {

    private final String BASIC_PROJECT_URL = "/api/project";
    private final String POST_URL = BASIC_PROJECT_URL;
    private final String GET_ALL_URL = BASIC_PROJECT_URL;
    private final String PUT_UPDATE_NAME = "/task/updateName/";
    private final String PUT_UPDATE_STATUS = "/task/updateStatus/";
    private final String DELETE_URL = "/task/delete/";

    @Override
    void setUp() throws Exception {
        Instant createdAt;
        Project project;
        Technologies technology;
        UserSummary userSummary;
        User user;
        UserSummary mentor;
        Pageable pageable;
        List<Project> projectList;
        List<User> users;
        UserPrincipal userPrincipal;
        ProjectRequest projectRequest;
        Set<Project> projectSet;
        HashMap<Long, User> creatorMap;
        Page<Project> page;
        technology = Technologies.builder().technologyType(TechnologyType.FRONTEND).description("Some description").id(1l).title("Some title").build();
        createdAt = new SimpleDateFormat("yyyy-MM-dd").parse("2020-12-31").toInstant();
        users = new ArrayList<>();
        user = new User();
        user.setPrivateId(UUID.randomUUID());
        user.setPublicId(21l);
        user.setEmailVerified(true);
        user.setName("Some name");
        user.setProvider(AuthProvider.local);
        user.setPassword("Somepassword");
        user.setAge(12);
        user.setHiddenFromSearch(true);
        user.setImageUrl("SomeImageUrl");
        user.setUserStatus(UserStatus.LOOKING_FOR_A_JOB);
        user.setEmail("some@example.com");
        project = new Project();
        project.setBody("Some body");
        project.setDescription("Some description");
        project.setNumberOfSeats(12l);
        project.setTitle("Some title");
        project.setCreatedAt(createdAt);
        project.setCreatedBy(user.getPublicId());
        project.setCreatedAt(createdAt);
        project.setUpdatedBy(user.getPublicId());
        project.setId(12l);
        project.setName("Some Easy name");
        pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "createdAt");
        projectList = new ArrayList<>();
        projectList.add(project);
        page = MockUtil.createMockPage(projectList);
        userPrincipal = UserPrincipal.create(user);
        projectRequest = new ProjectRequest();
        projectRequest.setBody(project.getBody());
        projectRequest.setDescription(project.getDescription());
        projectRequest.setName(project.getName());
        projectRequest.setNumberOfSeats(project.getNumberOfSeats());
        projectRequest.setRepository(project.getRepository());
        projectRequest.setTitle(project.getTitle());
        technology.setCreatedAt(createdAt);
        technology.setUpdatedAt(createdAt);
        technology.setCreatedBy(user.getPublicId());
        technology.setUpdatedBy(user.getPublicId());
        project.getTechnologies().add(technology);
        project.setRepository("Some Repo");
        project.setUpdatedAt(createdAt);
        projectSet = new HashSet<>();
        projectSet.add(project);
        technology.setProjects(projectSet);
    }

    @Test
    public void should_getAllProject() throws Exception {
        mockMvc.perform(get(GET_ALL_URL))
                .andExpect(status().isOk());
    }
}
