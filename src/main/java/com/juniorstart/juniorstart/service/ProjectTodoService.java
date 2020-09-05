package com.juniorstart.juniorstart.service;

import com.juniorstart.juniorstart.exception.BadRequestException;
import com.juniorstart.juniorstart.exception.ResourceNotFoundException;
import com.juniorstart.juniorstart.model.Project;
import com.juniorstart.juniorstart.model.ProjectTodo;
import com.juniorstart.juniorstart.model.User;
import com.juniorstart.juniorstart.payload.*;
import com.juniorstart.juniorstart.repository.ProjectRepository;
import com.juniorstart.juniorstart.repository.ProjectTodoRepository;
import com.juniorstart.juniorstart.repository.UserDao;
import com.juniorstart.juniorstart.security.UserPrincipal;
import com.juniorstart.juniorstart.util.ModelMapper;
import com.juniorstart.juniorstart.util.ValidatePageUtil;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/** Represents an project todo service which manipulate todos of project data. 20-09-2020.
 * @author Grzegorz Szczęsny
 * @version 1.0
 * @since 1.0
 */
@Service
@Slf4j
public class ProjectTodoService {


    public ProjectTodoService(ProjectTodoRepository projectTodoRepository, UserDao userDao, ProjectRepository projectRepository) {
        this.projectTodoRepository = projectTodoRepository;
        this.userDao = userDao;
        this.projectRepository = projectRepository;

    }

    final private ProjectTodoRepository projectTodoRepository;
    final private UserDao userDao;
    final private ProjectRepository projectRepository;


    /**
     * Get all todos of projects.
     * @param currentUser A current user's credentials.
     * @param page A page number.
     * @param size A page size.
     * @return a {@link PagedResponse} with a {@link ProjectResponse}.
     */
    public PagedResponse<ProjectTodoResponse> findAll(UserPrincipal currentUser, int page, int size) {
        ValidatePageUtil.validatePageNumberAndSize(page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        Page<ProjectTodo> projectTodos = projectTodoRepository.findAll(pageable);

        if(projectTodos.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), projectTodos.getNumber(), projectTodos.getSize(), projectTodos.getTotalElements(), projectTodos.getTotalPages(), projectTodos.isLast());
        }



        List<ProjectTodoResponse> projectTodoResponses = projectTodos.map(projectTodo -> {

            return ModelMapper.mapProjectTodoToProjectTodoResponse(projectTodo, projectTodo.getUser());

        }).getContent();


        return new PagedResponse<>(projectTodoResponses, projectTodos.getNumber(), projectTodos.getSize(), projectTodos.getTotalElements(), projectTodos.getTotalPages(), projectTodos.isLast());
    }

    /**
     * Create a project todo
     * @param projectTodoRequest a todo request
     * @param currentUser a current user's credentials.
     * @return a paged response of todos.
     */
    public ProjectTodoResponse createProjectTodo(ProjectTodoRequest projectTodoRequest, UserPrincipal currentUser) {

        User user = userDao.findById(currentUser.getId()).orElseThrow(() -> new ResourceNotFoundException("User", "userId", currentUser.getId()));
        ProjectTodo projectTodo = new ProjectTodo();

        projectTodo.setTitle(projectTodoRequest.getTitle());
        projectTodo.setUser(user);

        return projectRepository.findById(projectTodoRequest.getProjectId()).map(project -> {
            projectTodo.setProject(project);
            ProjectTodo createdProjectTodo = projectTodoRepository.save(projectTodo);
            return ModelMapper.mapProjectTodoToProjectTodoResponse(projectTodo, user);
        }).orElseThrow(() -> new ResourceNotFoundException("Project", "projectId", projectTodoRequest.getProjectId()));

    }

    /**
     * Get todos by identification number.
     * @param currentUser a current user's credentials.
     * @param projectTodoId a project todo identification number.
     * @return a paged response of todos
     */
    public ProjectTodoResponse findProjectTodoById(UserPrincipal currentUser, Long projectTodoId) {
        User user = userDao.findById(currentUser.getId()).orElseThrow(() -> new ResourceNotFoundException("User", "userId", currentUser.getId()));

        ProjectTodo projectTodo = projectTodoRepository.findById(projectTodoId).orElseThrow(() -> new ResourceNotFoundException("ProjectTodo", "projectTodoId", projectTodoId));

        return ModelMapper.mapProjectTodoToProjectTodoResponse(projectTodo, user);
    }

    /**
     * Get todos by title
     * @param title A todo's title.
     * @param page A page number.
     * @param size A page size.
     * @return a paged response of todos.
     */
    public PagedResponse<ProjectTodoResponse> findProjectTodoByTitle(String title, int page, int size) {

        ValidatePageUtil.validatePageNumberAndSize(page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");

        Page<ProjectTodo> projectTodos = projectTodoRepository.findByTitle(title, pageable);

        if(projectTodos.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), projectTodos.getNumber(), projectTodos.getSize(), projectTodos.getTotalElements(),projectTodos.getTotalPages(), projectTodos.isLast());
        }

        List<ProjectTodoResponse> projectTodoResponses = projectTodos.map(projectTodo -> {

            return ModelMapper.mapProjectTodoToProjectTodoResponse(projectTodo, projectTodo.getUser());

        }).getContent();

        return new PagedResponse<>(projectTodoResponses, projectTodos.getNumber(), projectTodos.getSize(), projectTodos.getTotalElements(), projectTodos.getTotalPages(), projectTodos.isLast());
    }

    /**
     * Get todos by creator.
     * @param currentUser A current user's credentials.
     * @param email A user's email address.
     * @param page A page number.
     * @param size A page size.
     * @return a paged response of todos.
     */
    public PagedResponse<ProjectTodoResponse> findByCreatedBy(UserPrincipal currentUser, String email, int page, int size) {
        ValidatePageUtil.validatePageNumberAndSize(page, size);
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");

        User user = userDao.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("Email", "email", email));

        Page<ProjectTodo> projectTodos = projectTodoRepository.findByCreatedBy(user.getPublicId(), pageable);
        if(projectTodos.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), projectTodos.getNumber(), projectTodos.getSize(), projectTodos.getTotalElements(),projectTodos.getTotalPages(), projectTodos.isLast());
        }



        List<ProjectTodoResponse> projectTodoResponses = projectTodos.map(projectTodo -> {

            User creator = userDao.findByPublicId(projectTodo.getCreatedBy()).orElseThrow(() -> new ResourceNotFoundException("User", "userId", projectTodo.getId()));

            return ModelMapper.mapProjectTodoToProjectTodoResponse(projectTodo, projectTodo.getUser());

        }).getContent();

        return new PagedResponse<>(projectTodoResponses, projectTodos.getNumber(), projectTodos.getSize(), projectTodos.getTotalElements(), projectTodos.getTotalPages(), projectTodos.isLast());

    }

    /**
     * Delete todo by identification number.
     * @param currentUser A current user's credentials.
     * @param projectTodoId A todo's identification number.
     * @return a paged response
     */
    public ResponseEntity<?> deleteProjectTodo(UserPrincipal currentUser, long projectTodoId) {
        ProjectTodo projectTodo = projectTodoRepository.findById(projectTodoId).orElseThrow(() -> new ResourceNotFoundException("ProjectTodo", "projectTodoId", projectTodoId));

        User userAlreadyLogged = userDao.findById(currentUser.getId()).orElseThrow(() -> new ResourceNotFoundException("User", "userId", currentUser.getId()));

        long creatorId = userAlreadyLogged.getPublicId();
        long projectTodoCreatorId = projectTodo.getCreatedBy();

        if(creatorId == projectTodoCreatorId) {
            projectTodoRepository.delete(projectTodo);
            return ResponseEntity.ok(new ApiResponse(true, "Project Todo deleted successfully"));
        } else {
            throw new BadRequestException("Sorry you not created this project todo");
        }

    }

    /**
     * Update a project todo
     * @param currentUser A current's user credentials.
     * @param projectTodoId A todo identification number.
     * @param updateProjectTodoRequest A update request that contains a credentials.
     * @return a updated {@link ProjectTodoResponse}
     */
    public ProjectTodoResponse updateProjectTodo(UserPrincipal currentUser, Long projectTodoId, UpdateProjectTodoRequest updateProjectTodoRequest) {

        User currentLoggedUser = userDao.findById(currentUser.getId()).orElseThrow(() -> new ResourceNotFoundException("User", "userId", currentUser.getId()));

        ProjectTodo projectTodo = projectTodoRepository.findById(projectTodoId).orElseThrow(() -> new ResourceNotFoundException("ProjectTodo", "projectTodoId", projectTodoId));

        long creatorId = currentLoggedUser.getPublicId();
        long projectTodoCreatorId = projectTodo.getCreatedBy();

        if(creatorId == projectTodoCreatorId) {
            projectTodo.setTitle(updateProjectTodoRequest.getTitle());

            ProjectTodo updatedProjectTodo = projectTodoRepository.save(projectTodo);
            User projectTodoCreator = userDao.findByPublicId(projectTodoCreatorId).orElseThrow(() -> new ResourceNotFoundException("User", "userId", projectTodoCreatorId));

            return ModelMapper.mapProjectTodoToProjectTodoResponse(projectTodo, projectTodoCreator);
        } else {
            throw new BadRequestException("Sorry you not created this project todo");
        }


    }



}
