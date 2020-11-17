package com.juniorstart.juniorstart.controller;

import com.juniorstart.juniorstart.model.*;
import com.juniorstart.juniorstart.payload.ProjectRequest;
import com.juniorstart.juniorstart.repository.ProjectRepository;
import com.juniorstart.juniorstart.repository.TechnologiesRepository;
import com.juniorstart.juniorstart.repository.UserDao;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import org.springframework.security.test.context.support.WithUserDetails;
import static org.hamcrest.core.Is.is;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProjectControllerTest extends ControllerIntegrationTest {

    private final String BASIC_PROJECT_URL = "/api/project";
    private final String GET_ALL_URL = BASIC_PROJECT_URL;

    Project project;
    Technologies technology;
    User user;
    ProjectRequest projectRequest;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    TechnologiesRepository technologiesRepository;

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
                .build();

        project = Project.builder()
                .name("ProjectName")
                .title("Some title of project")
                .numberOfSeats(12l)
                .description("Description of project")
                .body("Body of project")
                .recruiting(false)
                .repository("repository-link")
                .build();

        projectRequest = ProjectRequest.builder()
                .title("NewProjecTitle")
                .repository("NewRepository")
                .numberOfSeats(12l)
                .name("SomeName")
                .description("SomeDescription")
                .body("SomeBody")
                .recruiting(false)
                .build();


        userDao.save(user);
        projectRepository.save(project);

    }


    @Test
    @WithUserDetails("someEmail")
    public void getAllProjects() throws Exception {
        mockMvc.perform(get(GET_ALL_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.[0].name",is(project.getName())))
                .andExpect(jsonPath("$.content.[0].title",is(project.getTitle())))
                .andExpect(jsonPath("$.content.[0].description",is(project.getDescription())))
                .andExpect(jsonPath("$.content.[0].repository",is(project.getRepository())))
                .andExpect(jsonPath("$.content.[0].recruiting",is(project.getRecruiting())))
                .andExpect(jsonPath("$.content.[0].numberOfSeats",is(12)));

    }

    @Test
    public void getProjectByTitle() throws Exception {
        mockMvc.perform(get(BASIC_PROJECT_URL + "/title/" + project.getTitle())).andExpect(status().isOk())
                .andExpect(jsonPath("$.content.[0].name",is(project.getName())))
                .andExpect(jsonPath("$.content.[0].title",is(project.getTitle())))
                .andExpect(jsonPath("$.content.[0].description",is(project.getDescription())))
                .andExpect(jsonPath("$.content.[0].repository",is(project.getRepository())))
                .andExpect(jsonPath("$.content.[0].recruiting",is(project.getRecruiting())))
                .andExpect(jsonPath("$.content.[0].numberOfSeats",is(12)));

    }

    @Test
    public void getProjectByName() throws Exception {
        mockMvc.perform(get(BASIC_PROJECT_URL + "/name/" + project.getName())).andExpect(status().isOk())
                .andExpect(jsonPath("$.name",is(project.getName())))
                .andExpect(jsonPath("$.description",is(project.getDescription())))
                .andExpect(jsonPath("$.repository",is(project.getRepository())))
                .andExpect(jsonPath("$.numberOfSeats",is(12)));
    }

    @Test
    @WithUserDetails("someEmail")
    public void createProject() throws Exception {

        String jsonRequest = gson.toJson(projectRequest);

        mockMvc.perform(post(BASIC_PROJECT_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name",is(projectRequest.getName())))
                .andExpect(jsonPath("$.description",is(projectRequest.getDescription())));
    }

    @Test
    @WithUserDetails("someEmail")
    public void deleteProject() throws Exception {
        mockMvc.perform(delete(BASIC_PROJECT_URL + "/" + project.getId())).andExpect(status().isOk())
                .andExpect(jsonPath("success",is(true)))
                .andExpect(jsonPath("message",is("Project deleted successfully"))).andReturn();
    }

    @Test
    @WithUserDetails("someEmail")
    public void updateProject() throws Exception {

        String jsonRequest = gson.toJson(projectRequest);

        mockMvc.perform(put(BASIC_PROJECT_URL + "/" + project.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name",is(projectRequest.getName())))
                .andExpect(jsonPath("$.description",is(projectRequest.getDescription())));
    }

}


