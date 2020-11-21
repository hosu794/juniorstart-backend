package com.juniorstart.juniorstart.controller;

import com.juniorstart.juniorstart.model.Project;
import com.juniorstart.juniorstart.payload.PagedResponse;
import com.juniorstart.juniorstart.payload.ProjectRequest;
import com.juniorstart.juniorstart.payload.ProjectResponse;
import com.juniorstart.juniorstart.security.CurrentUser;
import com.juniorstart.juniorstart.security.UserPrincipal;
import com.juniorstart.juniorstart.service.ProjectService;
import com.juniorstart.juniorstart.util.AppConstants;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/project")
public class ProjectController {

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    private final ProjectService projectService;

    @GetMapping
    public PagedResponse<ProjectResponse> findAll(@RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                  @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
        return projectService.getAllProjects(page, size);
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public Project createProject(@RequestBody @Valid ProjectRequest projectRequest) {
        return projectService.createProject(projectRequest);
    }

    @PutMapping("/{projectId}")
    @PreAuthorize("hasRole('USER')")
    public ProjectResponse updateProject(@CurrentUser UserPrincipal currentUser,
                                         @PathVariable UUID projectId,
                                         @RequestBody @Valid ProjectRequest projectRequest) {
        return projectService.updateProject(currentUser, projectId, projectRequest);
    }

    @DeleteMapping("/{projectId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> deleteProject(@CurrentUser UserPrincipal currentUser,
                                           @PathVariable UUID projectId) {
        return projectService.deleteProject(currentUser, projectId);
    }

    @GetMapping("/title/{title}")
    public PagedResponse<ProjectResponse> findByTitle(@CurrentUser UserPrincipal currentUser,
                                                      @PathVariable String title,
                                                      @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                      @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
        return projectService.findByTitle(title,page ,size);
    }

    @GetMapping("/name/{name}")
    public ProjectResponse findByName(@PathVariable String name) {
        return projectService.findByName(name);
    }

  
}
