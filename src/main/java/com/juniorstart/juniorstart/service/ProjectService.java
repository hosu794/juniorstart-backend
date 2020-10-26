package com.juniorstart.juniorstart.service;

import com.juniorstart.juniorstart.exception.ResourceNotFoundException;
import com.juniorstart.juniorstart.model.Project;
import com.juniorstart.juniorstart.model.User;
import com.juniorstart.juniorstart.payload.PagedResponse;
import com.juniorstart.juniorstart.payload.ProjectResponse;
import com.juniorstart.juniorstart.repository.ProjectRepository;
import com.juniorstart.juniorstart.repository.TechnologiesRepository;
import com.juniorstart.juniorstart.repository.UserDao;
import com.juniorstart.juniorstart.util.ModelMapper;
import com.juniorstart.juniorstart.util.ValidatePageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;


/** Represents an project service which manipulate project data. 26-10-2020.
 * @author Grzegorz Szczęsny
 * @version 1.0
 * @since 1.0
 */
@Service
@Slf4j
public class ProjectService {


    public ProjectService(UserDao userDao, ProjectRepository projectRepository, TechnologiesRepository technologiesRepository) {
        this.userDao = userDao;
        this.projectRepository = projectRepository;
        this.technologiesRepository = technologiesRepository;
    }
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

            User creator = userDao.findByPublicId(project.getCreatedBy()).orElseThrow(()-> new ResourceNotFoundException("User", "userId", project.getCreatedBy()));

            return ModelMapper.mapProjectToProjectResponse(project, creator);
        }).getContent();

        return new PagedResponse<>(projectResponses, projects.getNumber(), projects.getSize(), projects.getTotalElements(), projects.getTotalPages(), projects.isLast());
        
    }
}
