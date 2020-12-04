package com.juniorstart.juniorstart.service;

import com.juniorstart.juniorstart.exception.BadRequestException;
import com.juniorstart.juniorstart.exception.ResourceNotFoundException;
import com.juniorstart.juniorstart.model.Project;
import com.juniorstart.juniorstart.model.Technologies;
import com.juniorstart.juniorstart.model.User;
import com.juniorstart.juniorstart.payload.ApiResponse;
import com.juniorstart.juniorstart.payload.PagedResponse;
import com.juniorstart.juniorstart.payload.ProjectRequest;
import com.juniorstart.juniorstart.payload.ProjectResponse;
import com.juniorstart.juniorstart.repository.ProjectRepository;
import com.juniorstart.juniorstart.repository.TechnologiesRepository;
import com.juniorstart.juniorstart.repository.UserDao;
import com.juniorstart.juniorstart.security.UserPrincipal;
import com.juniorstart.juniorstart.util.ModelMapper;
import com.juniorstart.juniorstart.util.ValidatePageUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


/** Represents an project service which manipulate project data. 26-10-2020.
 * @author Grzegorz Szczęsny
 * @version 1.0
 * @since 1.0
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ProjectService {

    private final UserDao userDao;
    private final ProjectRepository projectRepository;
    private final TechnologiesRepository technologiesRepository;

    /** Represents an project service which manipulate project data. 26-10-2020.
     * @author Grzegorz Szczęsny
     * @version 1.0
     * @since 1.0
     */
    public PagedResponse<ProjectResponse> getAllProjects(int page,
                                                         int size) {
        ValidatePageUtil.validatePageNumberAndSize(page, size);

        Pageable pageable = createPageableByCreatedAt(page, size);

        Page<Project> projects = queryPageWithAllProjects(pageable);

        if(isPageEmpty(projects)) {
            return emptyPagedResponse(projects);
        }

        List<ProjectResponse> projectResponses = createPagedResponses(projects);

        return responsePagedResponse(projects, projectResponses);

    }

    /**
     * Create a new project
     * @param projectRequest A request that contains a credentials for a new project.
     * @return a new {@link Project}.
     * @throws BadRequestException if user has too many projects.
     */
    public Project createProject(ProjectRequest projectRequest) {

        if(isProjectPresent(projectRequest)) {
            throw new BadRequestException("Name already in use.");
        }

        Project newProject = createProjectModel(projectRequest);

        return projectRepository.save(newProject);

    }

    /**
     * Update a existing project.
     * @param currentUser A current user's credentials.
     * @param projectId A project identification number.
     * @param projectRequest A request thar contains a credentials for a update project.
     * @return a updated project.
     * @throws BadRequestException if user hasn't has a ownership of project.
     */
    public ProjectResponse updateProject(UserPrincipal currentUser,
                                         UUID projectId,
                                         ProjectRequest projectRequest) {

        User user = queryUserByPrivateId(currentUser);


        Project currentProject = queryProjectById(projectId);

        boolean isCurrentUserIsOwnerOfProject = checkIsUserCreatedProject(currentProject, user);

        if(isCurrentUserIsOwnerOfProject) {

            Project updatedProject = updateProjectAndSave(projectRequest, currentProject);

            return ModelMapper.mapProjectToProjectResponse(updatedProject);
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
    public ResponseEntity<?> deleteProject(UserPrincipal currentUser, UUID projectId) {

        Project currentProject = queryProjectById(projectId);

        User currentLoggedUser = queryUserByPrivateId(currentUser);

        boolean isUserCreatedProject = checkIsUserCreatedProject(currentProject, currentLoggedUser);

        return deleteProjectIfUserCorrectOtherwiseThrowException(isUserCreatedProject, currentProject);
    }

    /**
     * Find all projects by title.
     * @param title The project's title
     * @param page A page number
     * @param size A page size
     * @return a {@link PagedResponse} with a {@link ProjectResponse}.
     */
    public PagedResponse<ProjectResponse> findByTitle(String title, int page, int size) {

        Page<Project> projects = validatePageAndFindByTitle(page, size, title);

        if(isPageEmpty(projects)) {
            return emptyPagedResponse(projects);
        }

        List<ProjectResponse> projectResponses = createPagedResponses(projects);

        return responsePagedResponse(projects, projectResponses);

    }

    /**
     * Find all projects by name
     * @param name The project's name.
     * @return a {@link PagedResponse} with a {@link ProjectResponse}.
     */
    public ProjectResponse findByName(String name){

        Project project = queryProjectByName(name);

        return ModelMapper.mapProjectToProjectResponse(project);
    }

    /**
     * Find all projects by technology's identification number.
     * @param technologyId A technology's identification number.
     * @param page A page number.
     * @param size A page size.
     * @return a {@link PagedResponse} with a {@link ProjectResponse}.
     */
    public PagedResponse<ProjectResponse> findByTechnology(Long technologyId,
                                                           int page,
                                                           int size) {

        Page<Project> projects = validatePageAndFindProjectsByIds(page, size, technologyId);

        if(isPageEmpty(projects)) {
            return emptyPagedResponse(projects);
        }

        List<ProjectResponse> projectResponses = createPagedResponses(projects);

         return responsePagedResponse(projects, projectResponses);
    }

    /*
    Helper functions
     */

    private User queryUserByPrivateId(UserPrincipal currentUser) {
        return userDao.findByPrivateId(currentUser.getId()).orElseThrow(() -> new ResourceNotFoundException("User", "userId", currentUser.getId()));
    }

    private Project queryProjectById(UUID id) {
        return projectRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Project", "projectId", id));
    }

    private User queryUserByPublicId(Project project) {
        return userDao.findByPublicId(project.getCreatedBy()).orElseThrow(() -> new ResourceNotFoundException("User", "userId", project.getCreatedBy()));
    }

    private boolean checkIsUserCreatedProject(Project project, User currentUser) {
        return project.getCreatedBy() == currentUser.getPublicId();
    }

    private Project createProjectModel(ProjectRequest projectRequest) {

        return Project.builder()
                .repository(projectRequest.getRepository())
                .name(projectRequest.getName())
                .title(projectRequest.getTitle())
                .numberOfSeats(projectRequest.getNumberOfSeats())
                .description(projectRequest.getDescription())
                .body(projectRequest.getBody())
                .build();

    }

    private ResponseEntity<?> deleteProjectIfUserCorrectOtherwiseThrowException(boolean isUserCreatedProject, Project currentProject) {
        if(isUserCreatedProject) {
            projectRepository.delete(currentProject);
            return ResponseEntity.ok(new ApiResponse(true, "Project deleted successfully"));
        } else {
            throw new BadRequestException("You not created that project!");
        }
    }



    private List<UUID> findIdsOfProject(Long technologyId) {
        Technologies technology = technologiesRepository.findById(technologyId).orElseThrow(() -> new ResourceNotFoundException("Technology", "technologyTitle", technologyId));
        return technology.getProjects().stream().map(Project::getId).collect(Collectors.toList());
    }

    private Project updateProjectAndSave(ProjectRequest projectRequest, Project currentProject) {
        currentProject = Project.builder()
                .recruiting(projectRequest.isRecruiting())
                .body(projectRequest.getBody())
                .description(projectRequest.getDescription())
                .numberOfSeats(projectRequest.getNumberOfSeats())
                .title(projectRequest.getTitle())
                .name(projectRequest.getName())
                .repository(projectRequest.getRepository()).build();

        return projectRepository.save(currentProject);
    }



    private Page<Project> validatePageAndFindProjectsByIds(int page, int size, long technologyId) {
        ValidatePageUtil.validatePageNumberAndSize(page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");

        return projectRepository.findByIdIn(findIdsOfProject(technologyId), pageable);
    }

    private Page<Project> validatePageAndFindByTitle(int page, int size, String title) {
        ValidatePageUtil.validatePageNumberAndSize(page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");

        return projectRepository.findByTitle(title, pageable);
    }

    private PagedResponse<ProjectResponse> responsePagedResponse(Page<Project> projects, List<ProjectResponse> projectResponses) {
        return new PagedResponse<>(projectResponses,
                projects.getNumber(),
                projects.getSize(),
                projects.getTotalElements(),
                projects.getTotalPages(),
                projects.isLast());
    }

    private Pageable createPageableByCreatedAt(int page, int size) {

        return PageRequest.of(
                page,
                size,
                Sort.Direction.DESC,
                "createdAt");

    }

    private Page<Project> queryPageWithAllProjects(Pageable pageable) {
        return projectRepository.findAll(pageable);
    }

    private PagedResponse<ProjectResponse> emptyPagedResponse(Page<Project> projects) {

        return new PagedResponse<>(
                Collections.emptyList(),
                projects.getNumber(),
                projects.getSize(),
                projects.getTotalElements(),
                projects.getTotalPages(),
                projects.isLast());

    }

    private boolean isPageEmpty(Page<Project> projects) {
        return projects.getNumberOfElements() == 0;
    }

    private List<ProjectResponse> createPagedResponses(Page<Project> projects) {
        return projects.map(project ->  ModelMapper.mapProjectToProjectResponse(project)).getContent();
    }

    private boolean isProjectPresent(ProjectRequest projectRequest) {
        return projectRepository.findByName(projectRequest.getName()).isPresent();
    }

    private Project queryProjectByName(String name) {
        return projectRepository.findByName(name).orElseThrow(() -> new ResourceNotFoundException("Project", "name", name));
    }

}


