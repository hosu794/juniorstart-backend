package com.juniorstart.juniorstart.controller;


import com.juniorstart.juniorstart.payload.*;
import com.juniorstart.juniorstart.security.CurrentUser;
import com.juniorstart.juniorstart.security.UserPrincipal;
import com.juniorstart.juniorstart.service.ProjectTodoService;
import com.juniorstart.juniorstart.util.AppConstants;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/api/project/todo")
public class ProjectTodoController {

    public ProjectTodoController(ProjectTodoService projectTodoService) {
        this.projectTodoService = projectTodoService;
    }

    private final ProjectTodoService projectTodoService;

    @GetMapping
    public PagedResponse<ProjectTodoResponse> findAll(@CurrentUser UserPrincipal currentUser, @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                               @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
        return projectTodoService.findAll(currentUser, page, size);
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ProjectTodoResponse createProjectTodo(@CurrentUser UserPrincipal currentUser, @RequestBody @Valid ProjectTodoRequest projectTodoRequest) {
        return projectTodoService.createProjectTodo(projectTodoRequest, currentUser);
    }

    @GetMapping("/id/{projectTodoId}")
    public ProjectTodoResponse findById(@CurrentUser UserPrincipal currentUser, @PathVariable Long projectTodoId) {
        return projectTodoService.findProjectTodoById(currentUser, projectTodoId);
    }

    @GetMapping("/title/{title}")
    public PagedResponse<ProjectTodoResponse> findByTitle(@CurrentUser UserPrincipal currentUser, @PathVariable String title, @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                   @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
        return projectTodoService.findProjectTodoByTitle(title, page, size);
    }

    @GetMapping("/username/{email}")
    public PagedResponse<ProjectTodoResponse> findByCreatedBy(@CurrentUser UserPrincipal currentUser, @PathVariable String email, @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                              @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
        return projectTodoService.findByCreatedBy(currentUser, email, page, size);
    }

    @DeleteMapping("/{projectTodoId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> deleteProjectTodo(@CurrentUser UserPrincipal currentUser, @PathVariable Long projectTodoId) {
        return projectTodoService.deleteProjectTodo(currentUser, projectTodoId);

    }

    @PutMapping("/{projectTodoId}")
    @PreAuthorize("hasRole('USER')")
    public ProjectTodoResponse updateProjectTodo(@CurrentUser UserPrincipal currentUser, @PathVariable Long projectTodoId, @RequestBody UpdateProjectTodoRequest updateProjectTodoRequest) {
        return projectTodoService.updateProjectTodo(currentUser, projectTodoId, updateProjectTodoRequest);
    }








}
