package com.juniorstart.juniorstart.service;

import com.juniorstart.juniorstart.model.*;
import com.juniorstart.juniorstart.model.audit.UserStatus;
import com.juniorstart.juniorstart.payload.*;
import com.juniorstart.juniorstart.repository.ProjectRepository;
import com.juniorstart.juniorstart.repository.ProjectTodoRepository;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(MockitoExtension.class)
public class ProjectTodoServiceTest {

    ProjectTodoRepository projectTodoRepository = Mockito.mock(ProjectTodoRepository.class);
    UserDao userDao = Mockito.mock(UserDao.class);
    ProjectRepository projectRepository = Mockito.mock(ProjectRepository.class);

    ProjectTodoService projectTodoService = new ProjectTodoService(projectTodoRepository, userDao, projectRepository);

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
    List<ProjectComment> projectComments = new ArrayList<>();
    ProjectRequest projectRequest;
    ProjectLike projectLike;
    Set<Project> projectSet;
    HashMap<Long, User> creatorMap;
    CommentRequest commentRequest;
    CommentUpdateRequest commentUpdateRequest;
    ProjectTodoRequest projectTodoRequest;
    UpdateProjectTodoRequest updateProjectTodoRequest;

    @Before
    public void initialize() throws Exception {
        technology = Technology.builder().technologyType(TechnologyType.FRONTEND).description("Some description").id(1l).title("Some title").build();
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
        user.setCreatedAt(createdAt);
        user.setUpdatedAt(createdAt);
        users.add(user);

        projectComment = ProjectComment.builder().project(project).body("Some body").user(user).id(1l).build();
        projectComment.setCreatedBy(user.getPublicId());
        projectComment.setUpdatedBy(user.getPublicId());
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
        projectComments = new ArrayList<>();
        projectComments.add(projectComment);
        project.setProjectComments(projectComments);
        projectTodo = ProjectTodo.builder().user(user).title("Some title").project(project).id(1l).build();
        projectTodo.setCreatedAt(createdAt);
        projectTodo.setUpdatedAt(createdAt);
        projectTodo.setCreatedBy(user.getPublicId());
        projectTodo.setUpdatedBy(user.getPublicId());
        users.add(user);
        projectTodo.setUser(user);
        projectTodos.add(projectTodo);
        project.setProjectTodos(projectTodos);
        listOfLikes = new ArrayList<>();
        listOfLikes.add((long) 53412);
        listOfLikes.add((long) 1323232);
        listOfLikes.add((long) 13434);
        page = MockUtil.createMockPage(projectTodos);
        userPrincipal = UserPrincipal.create(user);
        projectRequest = new ProjectRequest();
        projectRequest.setBody(project.getBody());
        projectRequest.setDescription(project.getDescription());
        projectRequest.setName(project.getName());
        projectRequest.setNumberOfSeats(project.getNumberOfSeats());
        projectRequest.setRepository(project.getRepository());
        projectRequest.setTitle(project.getTitle());
        projectLike = new ProjectLike();
        projectLike.setUser(user);
        projectLike.setProject(project);
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
        commentRequest = new CommentRequest();
        commentRequest.setBody(projectComment.getBody());
        projectComment.setCreatedAt(createdAt);
        projectComment.setUpdatedAt(createdAt);
        projectComment.setProject(project);
        commentUpdateRequest = new CommentUpdateRequest();
        commentUpdateRequest.setBody("new Body");
        projectTodoRequest = ProjectTodoRequest.builder().projectId(projectTodo.getId()).title(projectTodo.getTitle()).build();
        updateProjectTodoRequest = UpdateProjectTodoRequest.builder().title("Some new title").build();
    }

    @Test
    public void should_findAll() throws Exception {
        Mockito.when(projectTodoRepository.findAll(pageable)).thenReturn(page);
        Mockito.when(userDao.findByPublicIdIn(listOfLikes)).thenReturn(users);
        Assert.assertTrue(projectTodoService.findAll(userPrincipal, 0, 10).getContent().get(0).getTitle().contains(projectTodo.getTitle()));
    }

    @Test
    public void should_createProjectTodo() throws Exception {
        Mockito.when(userDao.findById(ArgumentMatchers.any(UUID.class))).thenReturn(Optional.of(user));
        Mockito.when(projectRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(project));
        Assert.assertTrue(projectTodoService.createProjectTodo(projectTodoRequest, userPrincipal).getTitle().contains(projectTodo.getTitle()));
    }

    @Test
    public void should_findProjectTodoById() throws Exception {
        Mockito.when(userDao.findById(ArgumentMatchers.any(UUID.class))).thenReturn(Optional.of(user));
        Mockito.when(projectTodoRepository.findById(ArgumentMatchers.any(Long.class))).thenReturn(Optional.of(projectTodo));
        Assert.assertTrue(projectTodoService.findProjectTodoById(userPrincipal, projectTodo.getId()).getTitle().contains(projectTodo.getTitle()));
    }

    @Test
    public void should_findProjectTodoByTitle() throws Exception {
        Mockito.when(projectTodoRepository.findByTitle(ArgumentMatchers.anyString(), ArgumentMatchers.isA(Pageable.class))).thenReturn(page);
        Assert.assertTrue(projectTodoService.findProjectTodoByTitle(projectTodo.getTitle(), 0, 10).getContent().get(0).getTitle().contains(projectTodo.getTitle()));
    }

    @Test
    public void should_findByCreatedBy() throws Exception {
        Mockito.when(userDao.findByEmail(ArgumentMatchers.anyString())).thenReturn(Optional.of(user));
        Mockito.when(projectTodoRepository.findByCreatedBy(ArgumentMatchers.anyLong(), ArgumentMatchers.isA(Pageable.class))).thenReturn(page);
        Mockito.when(userDao.findByPublicId(ArgumentMatchers.anyLong())).thenReturn(Optional.of(user));
        Assert.assertTrue(projectTodoService.findByCreatedBy(userPrincipal, user.getEmail(), 0, 10).getContent().get(0).getTitle().contains(projectTodo.getTitle()));

    }

    @Test
    public void should_deleteProjectTodo() throws Exception {
        Mockito.when(projectTodoRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(projectTodo));
        Mockito.when(userDao.findById(ArgumentMatchers.any(UUID.class))).thenReturn(Optional.of(user));
       Assert.assertEquals(projectTodoService.deleteProjectTodo(userPrincipal, projectTodo.getId()).getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void should_updateProjectTodo() throws Exception {
        Mockito.when(userDao.findById(ArgumentMatchers.any(UUID.class))).thenReturn(Optional.of(user));
        Mockito.when(projectTodoRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(projectTodo));
        Mockito.when(userDao.findByPublicId(ArgumentMatchers.anyLong())).thenReturn(Optional.of(user));
        Assert.assertTrue(projectTodoService.updateProjectTodo(userPrincipal, projectTodo.getId(), updateProjectTodoRequest).getTitle().contains(projectTodo.getTitle()));

    }

}
