package com.juniorstart.juniorstart.service;

import com.juniorstart.juniorstart.model.*;
import com.juniorstart.juniorstart.model.audit.UserStatus;
import com.juniorstart.juniorstart.payload.*;
import com.juniorstart.juniorstart.repository.ProjectRepository;
import com.juniorstart.juniorstart.repository.TechnologyRepository;
import com.juniorstart.juniorstart.repository.UserDao;
import com.juniorstart.juniorstart.security.UserPrincipal;
import com.juniorstart.juniorstart.util.MockUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(MockitoExtension.class)
public class TechnologyServiceTest {

    TechnologyRepository technologyRepository = Mockito.mock(TechnologyRepository.class);
    UserDao userDao = Mockito.mock(UserDao.class);
    ProjectRepository projectRepository = Mockito.mock(ProjectRepository.class);

    TechnologyService technologyService = new TechnologyService(technologyRepository, userDao, projectRepository);

    Instant createdAt;
    Project project;
    Technology technology;
    ProjectTodo projectTodo;
    UserSummary userSummary;
    User user;
    UserSummary mentor;
    ProjectComment projectComment;
    Pageable pageable;
    List<Project> projectList;
    List<Long> listOfLikes;
    int total;
    int start;
    int end;
    List<Project> output;
    List<ProjectTodo> projectTodos = new ArrayList<>();
    PageImpl page;
    List<User> users;
    UserPrincipal userPrincipal;
    List<Technology> technologies = new ArrayList<>();
    ProjectRequest projectRequest;
    ProjectLike projectLike;
    Set<Project> projectSet;
    HashMap<Long, User> creatorMap;
    Set<Project> projects;
    TechnologyRequest technologyRequest;
    UpdateTechnologyRequest updateTechnologyRequest;

    @Before
    public void initialize() throws Exception {
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
        user.setCreatedAt(createdAt);
        user.setUpdatedAt(createdAt);
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
        technology = Technology.builder().technologyType(TechnologyType.FRONTEND).description("Some description").id(1l).title("Some title").build();
        technology.setCreatedBy(user.getPublicId());
        technology.setUpdatedBy(user.getPublicId());
        projects = new HashSet<>();
        projects.add(project);
        technology.setProjects(projects);
        createdAt = new SimpleDateFormat("yyyy-MM-dd").parse("2020-12-31").toInstant();
        users = new ArrayList<>();
        users.add(user);
        technologies.add(technology);
        page = MockUtil.createMockPage(technologies);
        userPrincipal = UserPrincipal.create(user);
        technologyRequest = TechnologyRequest.builder().technologyType(technology.getTechnologyType())
                .description(technology.getDescription()).title(technology.getTitle()).build();
        updateTechnologyRequest = UpdateTechnologyRequest.builder().technologyId(technology.getId())
                .technologyType(technology.getTechnologyType()).description(technology.getDescription())
                .title(technology.getTitle()).build();
    }

    @Test
    public void should_findAll() throws Exception {
        Mockito.when(technologyRepository.findAll(ArgumentMatchers.isA(Pageable.class))).thenReturn(page);
        Assert.assertTrue(technologyService.findAll(0, 10).getContent().get(0).getDescription().contains(technology.getDescription()));
    }

    @Test
    public void should_addToProject() throws Exception {
        Mockito.when(userDao.findById(ArgumentMatchers.any(UUID.class))).thenReturn(Optional.of(user));
        Mockito.when(projectRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(project));
        Mockito.when(technologyRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(technology));
        Assert.assertEquals(technologyService.addToProject(project.getId(), technology.getId(), userPrincipal).getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void should_deleteFromProject() throws Exception {
        Mockito.when(userDao.findById(ArgumentMatchers.any(UUID.class))).thenReturn(Optional.of(user));
        Mockito.when(technologyRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(technology));
        Mockito.when(projectRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(project));
        Assert.assertEquals(technologyService.deleteFromProject(project.getId(), technology.getId(), userPrincipal).getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void should_findById() throws Exception {
        Mockito.when(technologyRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(technology));
        Assert.assertTrue(technologyService.findById(technology.getId()).getDescription().contains(technology.getDescription()));
    }

    @Test
    public void should_createTechnology() throws Exception {
        Mockito.when(userDao.findById(ArgumentMatchers.any(UUID.class))).thenReturn(Optional.of(user));
        Mockito.when(technologyRepository.save(ArgumentMatchers.any(Technology.class))).thenReturn(technology);
        Assert.assertTrue(technologyService.createTechnology(userPrincipal, technologyRequest).getDescription().contains(technology.getDescription()));
    }

    @Test
    public void should_deleteTechnology() throws Exception {
        Mockito.when(technologyRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(technology));
        Mockito.when(userDao.findByPrivateId(ArgumentMatchers.any(UUID.class))).thenReturn(Optional.of(user));
        Assert.assertEquals(technologyService.deleteTechnology(userPrincipal, technology.getId()).getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void should_updateTechnology() throws Exception {
        Mockito.when(technologyRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(technology));
        Mockito.when(userDao.findByPrivateId(ArgumentMatchers.any(UUID.class))).thenReturn(Optional.of(user));
        Mockito.when(technologyRepository.save(ArgumentMatchers.any(Technology.class))).thenReturn(technology);
        Assert.assertTrue(technologyService.updateTechnology(userPrincipal, updateTechnologyRequest).getDescription().contains(technology.getDescription()));
    }





}
