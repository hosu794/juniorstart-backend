package com.juniorstart.juniorstart.service;

import com.juniorstart.juniorstart.exception.BadRequestException;
import com.juniorstart.juniorstart.exception.ResourceNotFoundException;
import com.juniorstart.juniorstart.model.Project;
import com.juniorstart.juniorstart.model.ProjectLike;
import com.juniorstart.juniorstart.model.Technology;
import com.juniorstart.juniorstart.payload.ProjectRequest;
import com.juniorstart.juniorstart.model.User;
import com.juniorstart.juniorstart.payload.ApiResponse;
import com.juniorstart.juniorstart.payload.PagedResponse;
import com.juniorstart.juniorstart.payload.ProjectResponse;
import com.juniorstart.juniorstart.repository.ProjectLikeRepository;
import com.juniorstart.juniorstart.repository.ProjectRepository;
import com.juniorstart.juniorstart.repository.TechnologyRepository;
import com.juniorstart.juniorstart.repository.UserDao;
import com.juniorstart.juniorstart.security.UserPrincipal;
import com.juniorstart.juniorstart.util.ModelMapper;
import com.juniorstart.juniorstart.util.ValidatePageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
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

/** Represents an project service which manipulate project data. 20-09-2020.
 * @author Grzegorz Szczęsny
 * @version 1.0
 * @since 1.0
 */
@Service
@Slf4j
public class ProjectService {

    public ProjectService(ProjectRepository projectRepository, UserDao userDao, ProjectLikeRepository projectLikeRepository, TechnologyRepository technologyRepository) {
        this.projectRepository = projectRepository;
        this.userDao = userDao;
        this.projectLikeRepository = projectLikeRepository;
        this.technologyRepository = technologyRepository;
    }

    private final ProjectRepository projectRepository;
    private final UserDao userDao;
    private final ProjectLikeRepository projectLikeRepository;
    private final TechnologyRepository technologyRepository;

    /**
     * Get all projects.
     * @param currentUser A current user's credentials.
     * @param page A page number
     * @param size A page size
     * @return {@link PagedResponse} with a {@link ProjectResponse}
     */
    public PagedResponse<ProjectResponse> findAll(UserPrincipal currentUser, int page, int size) {

        ValidatePageUtil.validatePageNumberAndSize(page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        Page<Project> projects = projectRepository.findAll(pageable);

        if(projects.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), projects.getNumber(), projects.getSize(), projects.getTotalElements(), projects.getTotalPages(), projects.isLast());
        }

        List<ProjectResponse> projectResponses = projects.map(project -> {

            User creator = userDao.findByPublicId(project.getCreatedBy()).orElseThrow(()-> new ResourceNotFoundException("User", "userId", project.getCreatedBy()));

            long userProjectLike = projectLikeRepository.countByProjectId(project.getId());

            return ModelMapper.mapProjectToProjectResponse(project, creator, userProjectLike);
        }).getContent();

        return new PagedResponse<>(projectResponses, projects.getNumber(), projects.getSize(), projects.getTotalElements(), projects.getTotalPages(), projects.isLast());

    }

    /**
     * Create a new project
     * @param currentUser A current user's credentials
     * @param projectRequest A request that contains a credentials for a new project.
     * @return a new {@link Project}.
     * @throws BadRequestException if user has too many projects.
     */
    public Project createProject(UserPrincipal currentUser, ProjectRequest projectRequest) {

        User user = userDao.findById(currentUser.getId()).orElseThrow(() -> new ResourceNotFoundException("User", "userId", currentUser.getId()));

        boolean isUserCanCreateProject = user.getProjects().size() < 4;

        if(userDao.findByEmail(projectRequest.getName()).isPresent()) {
            throw new BadRequestException("Name already in use.");
        }

        if(!isUserCanCreateProject) {
            throw new BadRequestException("You have already maximum of projects");
        } else {

            Project newProject = new Project();
            newProject.setRepository(projectRequest.getRepository());
            newProject.setName(projectRequest.getName());
            newProject.setTitle(projectRequest.getTitle());
            newProject.setNumberOfSeats(projectRequest.getNumberOfSeats());
            newProject.setDescription(projectRequest.getDescription());
            newProject.setBody(projectRequest.getBody());

            return projectRepository.save(newProject);

        }
    }

    /**
     * Update a existing project.
     * @param currentUser A current user's credentials.
     * @param projectId A project identification number.
     * @param projectRequest A request thar contains a credentials for a update project.
     * @return a updated project.
     * @throws BadRequestException if user hasn't has a ownership of project.
     */
    public ProjectResponse updateProject(UserPrincipal currentUser, Long projectId, ProjectRequest projectRequest) {
        User user = userDao.findById(currentUser.getId()).orElseThrow(() -> new ResourceNotFoundException("User", "userId", currentUser.getId()));

        Project currentProject = projectRepository.findById(projectId).orElseThrow(() -> new ResourceNotFoundException("Project", "projectId", projectId));

        long currentUserIdentification = user.getPublicId();
        long projectOwnerIdentification = currentProject.getCreatedBy();

        boolean isCurrentUserIsOwnerOfProject = currentUserIdentification == projectOwnerIdentification;

        if(isCurrentUserIsOwnerOfProject) {
            currentProject.setRecruiting(projectRequest.isRecruiting());
            currentProject.setBody(projectRequest.getBody());
            currentProject.setDescription(projectRequest.getDescription());
            currentProject.setNumberOfSeats(projectRequest.getNumberOfSeats());
            currentProject.setTitle(projectRequest.getTitle());
            currentProject.setName(projectRequest.getName());
            currentProject.setRepository(projectRequest.getRepository());

            Project updatedProject = projectRepository.save(currentProject);

            long projectLikeCount = projectLikeRepository.countByProjectId(projectId);

            return ModelMapper.mapProjectToProjectResponse(currentProject, user, projectLikeCount);
        } else {
            throw new BadRequestException("You are not a owner of this project!");
        }
    }

    /**
     * Delete a project
     * @param currentUser A current user's credentials.
     * @param projectId A project's identification number.
     * @return a {@link ResponseEntity} with the message.
     * @throws BadRequestException if user hasn't created a this project.
     */
    public ResponseEntity<?> deleteProject(UserPrincipal currentUser, Long projectId) {

        Project currentProject = projectRepository.findById(projectId).orElseThrow(() -> new ResourceNotFoundException("Project", "projectId", projectId));

        User currentLoggedUser = userDao.findByPrivateId(currentUser.getId()).orElseThrow(() -> new ResourceNotFoundException("User", "userId", currentProject.getId()));

        long currentProjectIdentification = currentProject.getCreatedBy();
        long currentUserIdentificationNumber = currentLoggedUser.getPublicId();

        boolean isUserCreatedProject = currentProjectIdentification == currentUserIdentificationNumber;

        if(isUserCreatedProject) {
            projectRepository.delete(currentProject);
            return ResponseEntity.ok(new ApiResponse(true, "Project deleted successfully"));
        } else {
            throw new BadRequestException("You not created that project!");
        }

    }

    /**
     * Add like to a project.
     * @param currentUser A current's user credentials.
     * @param projectId A project's identification number.
     * @return a {@link ProjectResponse} with a created like.
     * @throws BadRequestException if user has already casted a like in this project.
     */
    public ProjectResponse likeProject(UserPrincipal currentUser, Long projectId) {
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new ResourceNotFoundException("Project", "projectId", projectId));
        User user = userDao.findById(currentUser.getId()).orElseThrow(() -> new ResourceNotFoundException("User", "userId", currentUser.getId()));

        ProjectLike projectLike = new ProjectLike();

        projectLike.setProject(project);
        projectLike.setUser(user);

        try {
            projectLikeRepository.save(projectLike);
        } catch (DataIntegrityViolationException ex) {
            throw new BadRequestException("Sorry! You have already casted your like in this project.");
        }

        User projectCreator = userDao.findByPublicId(project.getCreatedBy()).orElseThrow(() -> new ResourceNotFoundException("User", "userId", project.getCreatedBy()));

        long totalProjectLike = projectLikeRepository.countByProjectId(projectId);

        return ModelMapper.mapProjectToProjectResponse(project, projectCreator, totalProjectLike);

    }

    /**
     * Delete existing project's like.
     * @param currentUser A current's user credentials.
     * @param projectId A project's identification number.
     * @return a @{@link ProjectResponse} without deleted like.
     */
    public ProjectResponse unlikeProject(UserPrincipal currentUser, Long projectId) {
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new ResourceNotFoundException("Project", "projectId", projectId));

        User currentLoggedUser = userDao.findByPrivateId(currentUser.getId()).orElseThrow(() -> new ResourceNotFoundException("User", "userId", currentUser.getId()));

        ProjectLike projectLike = projectLikeRepository.findByUserIdAndProjectId(currentUser.getId(), projectId);


        if(projectLike == null) {
            throw new BadRequestException("Sorry! You not cast this like");
        } else {
            User projectCreator = userDao.findByPublicId(project.getCreatedBy()).orElseThrow(() -> new ResourceNotFoundException("User", "userId", project.getCreatedBy()));
            projectLikeRepository.delete(projectLike);
            long totalLikes = projectLikeRepository.countByProjectId(projectId);
            return ModelMapper.mapProjectToProjectResponse(project, projectCreator, totalLikes);
        }

    }

    /**
     * Find all projects by title.
     * @param currentUser A current's user credentials.
     * @param title The project's title
     * @param page A page number
     * @param size A page size
     * @return a {@link PagedResponse} with a {@link ProjectResponse}.
     */
    public PagedResponse<ProjectResponse> findByTitle(UserPrincipal currentUser, String title, int page, int size) {
        ValidatePageUtil.validatePageNumberAndSize(page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        Page<Project> projects = projectRepository.findByTitle(title, pageable);

        if(projects.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), projects.getNumber(), projects.getSize(), projects.getTotalElements(), projects.getTotalPages(), projects.isLast());
        }

        List<ProjectResponse> projectResponses = projects.map(project -> {

            User user = userDao.findByPublicId(project.getCreatedBy()).orElseThrow(() -> new ResourceNotFoundException("User", "userId", project.getCreatedBy()));

            long userLikeCount = projectLikeRepository.countByProjectId(project.getId());

            return ModelMapper.mapProjectToProjectResponse(project, user, userLikeCount);

        }).getContent();

        return new PagedResponse<>(projectResponses, projects.getNumber(), projects.getSize(), projects.getTotalElements(), projects.getTotalPages(), projects.isLast());

    }

    /**
     * Find all projects by name
     * @param currentUser A current's user credentials.
     * @param name The project's name.
     * @return a {@link PagedResponse} with a {@link ProjectResponse}.
     */
    public ProjectResponse findByName(UserPrincipal currentUser, String name){

        Project project = projectRepository.findByName(name).orElseThrow(() -> new ResourceNotFoundException("Project", "name", name));

        User creatorOfProject = userDao.findByPublicId(project.getCreatedBy()).orElseThrow(() -> new ResourceNotFoundException("User", "userId", project.getCreatedBy()));

        long projectLikeCount = projectLikeRepository.countByProjectId(project.getId());

        return ModelMapper.mapProjectToProjectResponse(project, creatorOfProject, projectLikeCount);
    }

    /**
     * Find all projects by technology's identification number.
     * @param currentUser A current's user credentials
     * @param technologyId A technology's identification number.
     * @param page A page number.
     * @param size A page size.
     * @return a {@link PagedResponse} with a {@link ProjectResponse}.
     */
    public PagedResponse<ProjectResponse> findByTechnology(UserPrincipal currentUser, Long technologyId, int page, int size) {
        ValidatePageUtil.validatePageNumberAndSize(page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");

        Technology technology = technologyRepository.findById(technologyId).orElseThrow(() -> new ResourceNotFoundException("Technology", "technologyTitle", technologyId));
        List<Long> longsOfProjects = technology.getProjects().stream().map(Project::getId).collect(Collectors.toList());
        Page<Project> projects = projectRepository.findByIdIn(longsOfProjects, pageable);

        if(projects.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), projects.getNumber(), projects.getSize(), projects.getTotalElements(), projects.getTotalPages(), projects.isLast());
        }

        List<ProjectResponse> projectResponses = projects.map(project -> {

            User user = userDao.findByPublicId(project.getCreatedBy()).orElseThrow(() -> new ResourceNotFoundException("User", "userId", project.getCreatedBy()));

            long userLikeCount = projectLikeRepository.countByProjectId(project.getId());

            return ModelMapper.mapProjectToProjectResponse(project, user, userLikeCount);

        }).getContent();

        return new PagedResponse<>(projectResponses, projects.getNumber(), projects.getSize(), projects.getTotalElements(), projects.getTotalPages(), projects.isLast());


    }



}
