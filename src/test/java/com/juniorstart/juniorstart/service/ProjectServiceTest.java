package com.juniorstart.juniorstart.service;

import com.juniorstart.juniorstart.model.*;
import com.juniorstart.juniorstart.model.audit.UserStatus;
import com.juniorstart.juniorstart.payload.*;
import com.juniorstart.juniorstart.repository.ProjectRepository;
import com.juniorstart.juniorstart.repository.TechnologiesRepository;
import com.juniorstart.juniorstart.repository.UserDao;
import com.juniorstart.juniorstart.security.UserPrincipal;
import com.juniorstart.juniorstart.util.MockUtil;
import com.nimbusds.oauth2.sdk.http.HTTPResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;


import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class ProjectServiceTest {

    @Mock
    ProjectRepository projectRepository;

    @Mock
    UserDao userDao;

    @Mock
    TechnologiesRepository technologyRepository;

    @InjectMocks
    ProjectService projectService;

    Instant createdAt;
    Project project;
    Technologies technology;
    User user;
    List<Project> projectList;
    List<User> users;
    UserPrincipal userPrincipal;
    ProjectRequest projectRequest;
    Set<Project> projectSet;
    Page<Project> page;
    UUID uuid = new UUID(12l, 12l);

    private void initializeUser() throws Exception  {
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
    }

    private void initializeProject() throws Exception {
        project = new Project();
        project.setBody("Some body");
        project.setDescription("Some description");
        project.setNumberOfSeats(12l);
        project.setTitle("Some title");
        project.setCreatedAt(createdAt);
        project.setCreatedBy(user.getPublicId());
        project.setCreatedAt(createdAt);
        project.setUpdatedBy(user.getPublicId());
        project.setId(uuid);
        project.setName("Some Easy name");
    }

    private void initializeProjectRequest() throws Exception {
        projectRequest = new ProjectRequest();
        projectRequest.setBody(project.getBody());
        projectRequest.setDescription(project.getDescription());
        projectRequest.setName(project.getName());
        projectRequest.setNumberOfSeats(project.getNumberOfSeats());
        projectRequest.setRepository(project.getRepository());
        projectRequest.setTitle(project.getTitle());
    }

    private void initializeTechnology() throws Exception {
        technology = Technologies.builder().technologyType(TechnologyType.FRONTEND).description("Some description").id(1l).title("Some title").build();
        technology.setCreatedAt(createdAt);
        technology.setUpdatedAt(createdAt);
        technology.setCreatedBy(user.getPublicId());
        technology.setUpdatedBy(user.getPublicId());
    }

    @BeforeEach
    public void initialize() throws Exception {

        createdAt = new SimpleDateFormat("yyyy-MM-dd").parse("2020-12-31").toInstant();
        users = new ArrayList<>();
        initializeUser();
        initializeProject();
        projectList = new ArrayList<>();
        projectList.add(project);
        page = MockUtil.createMockPage(projectList);
        userPrincipal = UserPrincipal.create(user);
        initializeProjectRequest();;
        initializeTechnology();
        project.getTechnologies().add(technology);
        project.setRepository("Some Repo");
        project.setUpdatedAt(createdAt);
        projectSet = new HashSet<>();
        projectSet.add(project);
        technology.setProjects(projectSet);
    }

    @Test
    public void should_return_findAll_method() throws Exception {

        when(userDao.findByPublicId(anyLong())).thenReturn(Optional.of(user));
        when(projectRepository.findAll(isA(Pageable.class))).thenReturn(page);


        PagedResponse<ProjectResponse> response = projectService.getAllProjects(0 , 10);

        assertTrue(response.getContent().get(0).getBody().contains(project.getBody()));
        assertTrue(response.getContent().get(0).getDescription().contains(project.getDescription()));
        assertTrue(response.getContent().get(0).getName().contains(project.getName()));
        assertTrue(response.getContent().get(0).getTitle().contains(project.getTitle()));

        verify(projectRepository, times(1)).findAll(isA(Pageable.class));
        verify(userDao, times(1)).findByPublicId(anyLong());
    }


    @Test
    public void should_create_new_project() throws Exception {

        when(projectRepository.findByName(ArgumentMatchers.any(String.class))).thenReturn(Optional.empty());
        when(projectRepository.save(ArgumentMatchers.any(Project.class))).thenReturn(project);

        Project response = projectService.createProject(projectRequest);

        assertTrue(response.getBody().contains(project.getBody()));
        assertTrue(response.getName().contains(project.getName()));
        assertEquals(response.getRecruiting(), project.getRecruiting());

        verify(projectRepository, times(1)).findByName(anyString());
        verify(projectRepository, times(1)).save(any(Project.class));
    }

    @Test
    public void should_update_project() throws Exception {

       when(userDao.findByPrivateId(any(UUID.class))).thenReturn(Optional.of(user));
       when(projectRepository.findById(any(UUID.class))).thenReturn(Optional.of(project));
       when(projectRepository.save(any(Project.class))).thenReturn(project);
       when(userDao.findByPublicId(anyLong())).thenReturn(Optional.of(user));

       ProjectResponse response = projectService.updateProject(userPrincipal, project.getId(), projectRequest);

        assertTrue(response.getName().contains(project.getName()));
        assertTrue(response.getTitle().contains(project.getTitle()));
        assertTrue(response.getDescription().contains(project.getDescription()));

        verify(userDao, times(1)).findByPrivateId(any(UUID.class));
        verify(projectRepository, times(1)).findById(any(UUID.class));
        verify(projectRepository, times(1)).save(any(Project.class));
        verify(userDao, times(1)).findByPublicId(anyLong());
    }

    @Test
    public void should_delete_project() throws Exception {

        when(projectRepository.findById(any(UUID.class))).thenReturn(Optional.of(project));
        when(userDao.findByPrivateId(any(UUID.class))).thenReturn(Optional.of(user));

        ResponseEntity<?> response = projectService.deleteProject(userPrincipal, project.getId());

        assertEquals(response.getStatusCodeValue(), HTTPResponse.SC_OK);

        verify(projectRepository, times(1)).findById(any(UUID.class));
        verify(userDao, times(1)).findByPrivateId(any(UUID.class));

    }

    @Test
    public void should_find_by_Name() throws Exception {

        when(projectRepository.findByName(any(String.class))).thenReturn(Optional.of(project));
        when(userDao.findByPublicId(anyLong())).thenReturn(Optional.of(user));

        ProjectResponse response = projectService.findByName(project.getName());

        assertTrue(response.getDescription().contains(project.getDescription()));
        assertTrue(response.getBody().contains(project.getBody()));
        assertTrue(response.getName().contains(project.getName()));

        verify(projectRepository, times(1)).findByName(anyString());
        verify(userDao, times(1)).findByPublicId(anyLong());
    }

    @Test
    public void should_find_by_technology() throws Exception {

      when(technologyRepository.findById(anyLong())).thenReturn(Optional.of(technology));
      when(projectRepository.findByIdIn(anyList(), isA(Pageable.class))).thenReturn(page);
      when(userDao.findByPublicId(anyLong())).thenReturn(Optional.of(user));

      PagedResponse<ProjectResponse> response = projectService.findByTechnology(technology.getId(), 0, 10);

      assertTrue(response.getContent().get(0).getName().contains(project.getName()));
      assertTrue(response.getContent().get(0).getBody().contains(project.getBody()));
      assertTrue(response.getContent().get(0).getDescription().contains(project.getDescription()));

      verify(technologyRepository, times(1)).findById(anyLong());
      verify(projectRepository, times(1)).findByIdIn(anyList(), isA(Pageable.class));
      verify(userDao, times(1)).findByPublicId(anyLong());
    }
    
}
