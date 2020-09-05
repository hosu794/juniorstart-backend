package com.juniorstart.juniorstart.service;

import com.juniorstart.juniorstart.model.*;
import com.juniorstart.juniorstart.model.audit.UserStatus;
import com.juniorstart.juniorstart.payload.CommentRequest;
import com.juniorstart.juniorstart.payload.CommentUpdateRequest;
import com.juniorstart.juniorstart.payload.ProjectRequest;
import com.juniorstart.juniorstart.payload.UserSummary;
import com.juniorstart.juniorstart.repository.ProjectCommentRepository;
import com.juniorstart.juniorstart.repository.ProjectLikeRepository;
import com.juniorstart.juniorstart.repository.ProjectRepository;
import com.juniorstart.juniorstart.repository.UserDao;
import com.juniorstart.juniorstart.security.UserPrincipal;
import com.juniorstart.juniorstart.util.MockUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.RepeatedTest;
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
@ExtendWith({MockitoExtension.class})
public class ProjectCommentServiceTest {

    ProjectCommentRepository projectCommentRepository = Mockito.mock(ProjectCommentRepository.class);
    UserDao userDao = Mockito.mock(UserDao.class);
    ProjectRepository projectRepository = Mockito.mock(ProjectRepository.class);
    ProjectLikeRepository projectLikeRepository = Mockito.mock(ProjectLikeRepository.class);

    ProjectCommentService projectCommentService = new ProjectCommentService(projectCommentRepository, userDao, projectRepository, projectLikeRepository);

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



    @Before
    public void initialize() throws Exception {
        technology = Technology.builder().technologyType(TechnologyType.FRONTEND).description("Some description").id(1l).title("Some title").build();
        projectTodo = ProjectTodo.builder().user(user).title("Some title").project(project).id(1l).build();

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
        page = MockUtil.createMockPage(projectComments);
        users.add(user);
        projectTodo.setUser(user);
        projectTodos.add(projectTodo);
        project.setProjectTodos(projectTodos);
        listOfLikes = new ArrayList<>();
        listOfLikes.add((long) 53412);
        listOfLikes.add((long) 1323232);
        listOfLikes.add((long) 13434);
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
    }

    @Test
    public void should_create_comment() throws Exception {
        Mockito.when(userDao.findById(ArgumentMatchers.any(UUID.class))).thenReturn(Optional.of(user));
        Mockito.when(projectRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(project));
        Mockito.when(userDao.findByPublicId(ArgumentMatchers.anyLong())).thenReturn(Optional.of(user));
        Mockito.when(projectLikeRepository.countByProjectId(ArgumentMatchers.anyLong())).thenReturn(12l);
        Assert.assertTrue(projectCommentService.createComment(commentRequest, userPrincipal, project.getId()).getBody().contains(projectComment.getBody()));
    }

    @Test
    public void should_findById() throws Exception {
        Mockito.when(projectCommentRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(projectComment));
        Mockito.when(userDao.findByPublicId(ArgumentMatchers.anyLong())).thenReturn(Optional.of(user));
        Assert.assertTrue(projectCommentService.findById(projectComment.getId()).getBody().contains(projectComment.getBody()));
    }

    @Test
    public void should_deleteComment() throws Exception {
        Mockito.when(projectCommentRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(projectComment));
        Mockito.when(userDao.findById(ArgumentMatchers.any(UUID.class))).thenReturn(Optional.of(user));
        Assert.assertEquals(projectCommentService.deleteComment(projectComment.getId(), userPrincipal).getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void should_updateComment() throws Exception {
        Mockito.when(userDao.findById(ArgumentMatchers.any(UUID.class))).thenReturn(Optional.of(user));
        Mockito.when(projectCommentRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(projectComment));
        Assert.assertEquals(projectCommentService.updateComment(userPrincipal, projectComment.getId(), commentUpdateRequest).getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void should_findAllComment() throws Exception {
        Mockito.when(projectCommentRepository.findAll(ArgumentMatchers.isA(Pageable.class))).thenReturn(page);
        Mockito.when(userDao.findByPublicId(ArgumentMatchers.any(Long.class))).thenReturn(Optional.of(user));
        Mockito.when(projectLikeRepository.countByProjectId(ArgumentMatchers.anyLong())).thenReturn(12l);
        Assert.assertTrue(projectCommentService.findAllComment(userPrincipal, 0, 10).getContent().get(0).getBody().contains(projectComment.getBody()));

    }

    @Test
    public void should_findByProjectId() throws Exception {
        Mockito.when(projectRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(project));
        Mockito.when(projectCommentRepository.findByProjectId(ArgumentMatchers.anyLong(), ArgumentMatchers.isA(Pageable.class))).thenReturn(page);
        Mockito.when(userDao.findByPublicId(ArgumentMatchers.anyLong())).thenReturn(Optional.of(user));
        Mockito.when(projectLikeRepository.countByProjectId(ArgumentMatchers.anyLong())).thenReturn(12l);
        Assert.assertTrue(projectCommentService.findByProjectId(project.getId(), 0, 10).getContent().get(0).getBody().contains(projectComment.getBody()));
    }

    @Test
    public void should_findByCreateBy() throws Exception {
        Mockito.when(projectCommentRepository.findByCreatedBy(ArgumentMatchers.anyLong(), ArgumentMatchers.isA(Pageable.class))).thenReturn(page);
        Mockito.when(userDao.findByPublicId(ArgumentMatchers.anyLong())).thenReturn(Optional.of(user));
        Mockito.when(projectLikeRepository.countByProjectId(ArgumentMatchers.anyLong())).thenReturn(12l);
        Assert.assertTrue(projectCommentService.findByCreatedBy(user.getPublicId(), 0, 10).getContent().get(0).getBody().contains(projectComment.getBody()));
    }


}
