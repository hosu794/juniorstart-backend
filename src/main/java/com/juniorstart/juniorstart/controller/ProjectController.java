package com.juniorstart.juniorstart.controller;

import com.juniorstart.juniorstart.model.Project;
import com.juniorstart.juniorstart.payload.LikerRequest;
import com.juniorstart.juniorstart.payload.PagedResponse;
import com.juniorstart.juniorstart.payload.ProjectRequest;
import com.juniorstart.juniorstart.payload.ProjectResponse;
import com.juniorstart.juniorstart.security.CurrentUser;
import com.juniorstart.juniorstart.security.UserPrincipal;
import com.juniorstart.juniorstart.service.ProjectService;
import com.juniorstart.juniorstart.util.AppConstants;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/project")
public class ProjectController {

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    final private ProjectService projectService;

    @GetMapping
    public PagedResponse<ProjectResponse> findAll(@CurrentUser UserPrincipal currentUser, @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                           @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
        return projectService.findAll(currentUser, page, size);
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public Project createProject(@CurrentUser UserPrincipal currentUser, @RequestBody @Valid ProjectRequest projectRequest) {
        return projectService.createProject(currentUser, projectRequest);
    }

    @PutMapping("/{projectId}")
    @PreAuthorize("hasRole('USER')")
    public ProjectResponse updateProject(@CurrentUser UserPrincipal currentUser, @PathVariable Long projectId, @RequestBody @Valid ProjectRequest projectRequest) {
        return projectService.updateProject(currentUser, projectId, projectRequest);
    }

    @DeleteMapping("/{projectId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> deleteProject(@CurrentUser UserPrincipal currentUser, @PathVariable Long projectId) {
        return projectService.deleteProject(currentUser, projectId);
    }

    @PostMapping("/likes")
    @PreAuthorize("hasRole('USER')")
    public ProjectResponse castLike(@CurrentUser UserPrincipal currentUser, @RequestBody @Valid LikerRequest likeRequest) {
        return projectService.likeProject(currentUser, likeRequest.getProjectId());
    }

    @DeleteMapping("/likes/{projectId}")
    @PreAuthorize("hasRole('USER')")
    public ProjectResponse unCastLike(@CurrentUser UserPrincipal currentUser, @PathVariable Long projectId) {
        return projectService.unlikeProject(currentUser, projectId);
    }

    @GetMapping("/title/{title}")
    public PagedResponse<ProjectResponse> findByTitle(@CurrentUser UserPrincipal currentUser, @PathVariable String title, @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                      @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
        return projectService.findByTitle(currentUser, title,page ,size);
    }

    @GetMapping("/name/{name}")
    public ProjectResponse findByName(@CurrentUser UserPrincipal currentUser, @PathVariable String name) {
        return projectService.findByName(currentUser, name);
    }

    @GetMapping("/technology/{technologyId}")
    public PagedResponse<ProjectResponse> findByTechnology(@CurrentUser UserPrincipal currentUser, @PathVariable Long technologyId, @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                           @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
        return projectService.findByTechnology(currentUser, technologyId, page, size);
    }



}

