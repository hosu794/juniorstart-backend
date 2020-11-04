package com.juniorstart.juniorstart.service;

import com.juniorstart.juniorstart.exception.BadRequestException;
import com.juniorstart.juniorstart.exception.ResourceNotFoundException;
import com.juniorstart.juniorstart.model.Project;
import com.juniorstart.juniorstart.model.Technologies;
import com.juniorstart.juniorstart.model.User;
import com.juniorstart.juniorstart.model.UserProfile;
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
    public PagedResponse<ProjectResponse> getAllProjects(int page, int size) {
        ValidatePageUtil.validatePageNumberAndSize(page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        Page<Project> projects = projectRepository.findAll(pageable);

        if(projects.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), projects.getNumber(), projects.getSize(), projects.getTotalElements(), projects.getTotalPages(), projects.isLast());
        }

        List<ProjectResponse> projectResponses = projects.map(project -> {

            User creator = userDao.findByPublicId(project.getCreatedBy())
                    .orElseThrow(()-> new ResourceNotFoundException("User", "userId", project.getCreatedBy()));

            return ModelMapper.mapProjectToProjectResponse(project, creator);
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
        User user = userDao.findById(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "userId", currentUser.getId()));

        if(projectRepository.findByName(projectRequest.getName()).isPresent()) {
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
    public ProjectResponse updateProject(UserPrincipal currentUser, Long projectId, ProjectRequest projectRequest) {

        User user = userDao.findById(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "userId", currentUser.getId()));

        Project currentProject = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "id", projectId));

        long currentUserIdentification = user.getPublicId();
        long projectOwnerIdentification = currentProject.getCreatedBy();

        boolean isCurrentUserIsOwnerOfProject = currentUserIdentification == projectOwnerIdentification;

        if(isCurrentUserIsOwnerOfProject) {

            Project updatedProject = updateProjectModel(currentProject, projectRequest);
            Project result = projectRepository.save(updatedProject);

            return ModelMapper.mapProjectToProjectResponse(result, user);
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
        Project currentProject = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "projectId", projectId));

        User currentLoggedUser = userDao.findByPrivateId(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "userId", currentProject.getId()));

        boolean isUserCreatedProject = checkUserCreatedProject(currentLoggedUser, currentProject);

        if(isUserCreatedProject) {
            projectRepository.delete(currentProject);
            return ResponseEntity.ok(new ApiResponse(true, "Project deleted successfully"));
        } else {
            throw new BadRequestException("You not created that project!");
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

            User user = userDao.findByPublicId(project.getCreatedBy())
                    .orElseThrow(() -> new ResourceNotFoundException("User", "userId", project.getCreatedBy()));

            return ModelMapper.mapProjectToProjectResponse(project, user);

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

        Project project = projectRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "name", name));

        User creatorOfProject = userDao.findByPublicId(project.getCreatedBy())
                .orElseThrow(() -> new ResourceNotFoundException("User", "userId", project.getCreatedBy()));

        return ModelMapper.mapProjectToProjectResponse(project, creatorOfProject);
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

        Technologies technology = technologiesRepository.findById(technologyId)
                .orElseThrow(() -> new ResourceNotFoundException("Technology", "technologyTitle", technologyId));
        List<Long> longsOfProjects = technology.getProjects().stream().map(Project::getId).collect(Collectors.toList());
        Page<Project> projects = projectRepository.findByIdIn(longsOfProjects, pageable);

        if(projects.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), projects.getNumber(), projects.getSize(), projects.getTotalElements(), projects.getTotalPages(), projects.isLast());
        }

        List<ProjectResponse> projectResponses = projects.map(project -> {

            User user = userDao.findByPublicId(project.getCreatedBy())
                    .orElseThrow(() -> new ResourceNotFoundException("User", "userId", project.getCreatedBy()));

            return ModelMapper.mapProjectToProjectResponse(project, user);

        }).getContent();

        return new PagedResponse<>(projectResponses, projects.getNumber(), projects.getSize(), projects.getTotalElements(), projects.getTotalPages(), projects.isLast());


    }

    /**
     * Create a {@link Project} from {@link ProjectRequest}
     * @param projectRequest A request with a credentials.
     * @return A model ready to save.
     */
    private Project createProjectModel(ProjectRequest projectRequest) {
        Project newProject = new Project();
        newProject.setRepository(projectRequest.getRepository());
        newProject.setName(projectRequest.getName());
        newProject.setTitle(projectRequest.getTitle());
        newProject.setNumberOfSeats(projectRequest.getNumberOfSeats());
        newProject.setDescription(projectRequest.getDescription());
        newProject.setBody(projectRequest.getBody());

        return newProject;
    }

    /**
     * Update a {@link Project} with {@link ProjectRequest}
     * @param currentProject A current project's object.
     * @param projectRequest A projectRequest that will update a project
     * @return A updated project ready to save.
     */
    private Project updateProjectModel(Project currentProject, ProjectRequest projectRequest) {
        currentProject.setRecruiting(projectRequest.isRecruiting());
        currentProject.setBody(projectRequest.getBody());
        currentProject.setDescription(projectRequest.getDescription());
        currentProject.setNumberOfSeats(projectRequest.getNumberOfSeats());
        currentProject.setTitle(projectRequest.getTitle());
        currentProject.setName(projectRequest.getName());
        currentProject.setRepository(projectRequest.getRepository());

        return currentProject;
    }

    /**
     * Check is current logged user created a project.
     * @param currentLoggedUser A current user.
     * @param currentProject A project, that user want update.
     * @return A true if user created a project.
     */
    private boolean checkUserCreatedProject(User currentLoggedUser, Project currentProject) {
        long currentProjectIdentification = currentProject.getCreatedBy();
        long currentUserIdentificationNumber = currentLoggedUser.getPublicId();

        return currentProjectIdentification == currentUserIdentificationNumber;
    }


}
