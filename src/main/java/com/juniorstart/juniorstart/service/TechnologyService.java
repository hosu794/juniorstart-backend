package com.juniorstart.juniorstart.service;

import com.juniorstart.juniorstart.exception.BadRequestException;
import com.juniorstart.juniorstart.exception.ResourceNotFoundException;
import com.juniorstart.juniorstart.model.Project;
import com.juniorstart.juniorstart.model.Technology;
import com.juniorstart.juniorstart.model.TechnologyType;
import com.juniorstart.juniorstart.model.User;
import com.juniorstart.juniorstart.model.audit.UserDateAudit;
import com.juniorstart.juniorstart.payload.*;
import com.juniorstart.juniorstart.repository.ProjectRepository;
import com.juniorstart.juniorstart.repository.TechnologyRepository;
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
public class TechnologyService {

    public TechnologyService(TechnologyRepository technologyRepository, UserDao userDao, ProjectRepository projectRepository) {
        this.technologyRepository = technologyRepository;
        this.userDao = userDao;
        this.projectRepository = projectRepository;
    }

    final private TechnologyRepository technologyRepository;
    final private UserDao userDao;
    final private ProjectRepository projectRepository;

    /**
     * Get all technologies.
     * @param page A page number.
     * @param size A page size.
     * @return paged response of technologies responses.
     */
    public PagedResponse<TechnologyResponse> findAll(int page, int size) {
        ValidatePageUtil.validatePageNumberAndSize(page, size);


        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        Page<Technology> technologies = technologyRepository.findAll(pageable);

        if(technologies.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), technologies.getNumber(), technologies.getSize(), technologies.getTotalElements(), technologies.getTotalPages(), technologies.isLast());
        }

        List<TechnologyResponse> technologyResponses = technologies.map(technology -> {
            return ModelMapper.mapTechnologyToTechnologyResponse(technology);
        }).getContent();

        return new PagedResponse<>(technologyResponses, technologies.getNumber(), technologies.getSize(), technologies.getTotalElements(), technologies.getTotalPages(), technologies.isLast());

    }

    /**
     * Add technology to a project.
     * @param projectId A project identification number.
     * @param technologyId A technology identification number.
     * @param currentUser A current user's credentials.
     * @return a {@link ResponseEntity} with the message.
     */
    public ResponseEntity<?> addToProject(Long projectId, Long technologyId, UserPrincipal currentUser) {

        User currentLoggedUser = userDao.findById(currentUser.getId()).orElseThrow(() -> new ResourceNotFoundException("User", "userId", currentUser.getId()));
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new ResourceNotFoundException("Project", "projectId", projectId));
        Technology technology = technologyRepository.findById(technologyId).orElseThrow(() -> new ResourceNotFoundException("Technology", "technologyId", technologyId));

        long currentProjectIdentification = project.getCreatedBy();
        long currentUserIdentificationNumber = currentLoggedUser.getPublicId();

        boolean isUserCreatedProject = currentProjectIdentification == currentUserIdentificationNumber;

            if(isUserCreatedProject) {
            project.getTechnologies().add(technology);
            technology.getProjects().add(project);
            projectRepository.save(project);
            return ResponseEntity.ok(new ApiResponse(true, "Technology has added successfully to a project!"));
        } else {
            throw new BadRequestException("You are not have permission to add technology to this project!");
        }
    }

    /**
     * Delete a technology from project.
     * @param projectId A project identification number.
     * @param technologyId A technology identification number.
     * @param currentUser a current user's credentials.
     * @return a {@link ResponseEntity} with the message.
     */
    public ResponseEntity<?> deleteFromProject(Long projectId, Long technologyId, UserPrincipal currentUser) {
        User currentLoggedUser = userDao.findById(currentUser.getId()).orElseThrow(()-> new ResourceNotFoundException("User", "userId", currentUser.getId()));

        Technology technology = technologyRepository.findById(technologyId).orElseThrow(() -> new ResourceNotFoundException("Technology", "technologyId", technologyId));

        Project project = projectRepository.findById(projectId).orElseThrow(() -> new ResourceNotFoundException("Project", "projectId", projectId));

        long currentProjectIdentification = project.getCreatedBy();
        long currentUserIdentificationNumber = currentLoggedUser.getPublicId();

        boolean isUserCreatedProject = currentProjectIdentification == currentUserIdentificationNumber;

        if(isUserCreatedProject) {
            project.getTechnologies().remove(technology);
            return ResponseEntity.ok(new ApiResponse(true, "Technology deleted from project successfully"));
        } else {
            throw new BadRequestException("Sorry you not created this technology");
        }
    }

    /**
     * Get by identification number.
     * @param technologyId A technology identification number.
     * @return a {@link TechnologyResponse}
     */
    public TechnologyResponse findById(Long technologyId) {
        Technology technology = technologyRepository.findById(technologyId).orElseThrow(() -> new ResourceNotFoundException("Technology", "technology", technologyId));

        return ModelMapper.mapTechnologyToTechnologyResponse(technology);
    }

    /**
     * Create a technology.
     * @param currentUser A current user's credentials.
     * @param technologyRequest a request that contains a technology's credentials.
     * @return a created technology.
     */
    public Technology createTechnology(UserPrincipal currentUser, TechnologyRequest technologyRequest) {

        User user = userDao.findById(currentUser.getId()).orElseThrow(()-> new ResourceNotFoundException("User", "userId", currentUser.getId()));

        Technology newTechnology = new Technology();
        newTechnology.setDescription(technologyRequest.getDescription());
        newTechnology.setTechnologyType(technologyRequest.getTechnologyType());
        newTechnology.setTitle(technologyRequest.getTitle());

        return technologyRepository.save(newTechnology);
    }

    /**
     * Delete a technology.
     * @param currentUser A current user's credentials.
     * @param technologyId a technology identification number.
     * @return a {@link ResponseEntity} with the message.
     */
    public ResponseEntity<?> deleteTechnology(UserPrincipal currentUser, Long technologyId) {
        Technology technology = technologyRepository.findById(technologyId).orElseThrow(() -> new ResourceNotFoundException("Technology", "technologyId", technologyId));
        User currentLoggedUser = userDao.findByPrivateId(currentUser.getId()).orElseThrow(() -> new ResourceNotFoundException("User", "userId", currentUser.getId()));
        long creatorOfTechnologyIdentificationNumber = technology.getCreatedBy();
        long currentUserIdentificationNumber = currentLoggedUser.getPublicId();

        boolean isCurrentUserOwnerTechnology = creatorOfTechnologyIdentificationNumber == currentUserIdentificationNumber;

        if(isCurrentUserOwnerTechnology) {
            technologyRepository.delete(technology);
            return ResponseEntity.ok(new ApiResponse(true, "Technology deleted successfully"));
        } else {
            throw new BadRequestException("Sorry you not created this technology");
        }

    }

    /**
     * Update a technology.
     * @param currentUser a current user's credentials.
     * @param updateTechnologyRequest a request that contains a technology's credentials.
     * @return a updated technology.
     */
    public TechnologyResponse updateTechnology(UserPrincipal currentUser, UpdateTechnologyRequest updateTechnologyRequest) {
        Technology technology = technologyRepository.findById(updateTechnologyRequest.getTechnologyId()).orElseThrow(() -> new ResourceNotFoundException("Technology", "technologyId", updateTechnologyRequest.getTechnologyId()));
        User currentLoggedUser = userDao.findByPrivateId(currentUser.getId()).orElseThrow(()-> new ResourceNotFoundException("User", "userId", currentUser.getId()));

        long creatorOfTechnologyIdentificationNumber = technology.getCreatedBy();
        long currentUserIdentificationNumber = currentLoggedUser.getPublicId();

        boolean isCurrentUserOwnerTechnology = creatorOfTechnologyIdentificationNumber == currentUserIdentificationNumber;

        if(isCurrentUserOwnerTechnology) {
            technology.setTitle(updateTechnologyRequest.getTitle());
            technology.setTechnologyType(updateTechnologyRequest.getTechnologyType());
            technology.setDescription(updateTechnologyRequest.getDescription());
            Technology updatedTechnology = technologyRepository.save(technology);
            return ModelMapper.mapTechnologyToTechnologyResponse(updatedTechnology);
        } else {
            throw new BadRequestException("Sorry you not created this technology");
        }

    }



}
