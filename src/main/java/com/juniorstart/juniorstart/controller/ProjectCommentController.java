package com.juniorstart.juniorstart.controller;

import com.juniorstart.juniorstart.model.ProjectComment;
import com.juniorstart.juniorstart.payload.CommentRequest;
import com.juniorstart.juniorstart.payload.CommentUpdateRequest;
import com.juniorstart.juniorstart.payload.PagedResponse;
import com.juniorstart.juniorstart.payload.ProjectCommentResponse;
import com.juniorstart.juniorstart.repository.ProjectCommentRepository;
import com.juniorstart.juniorstart.security.CurrentUser;
import com.juniorstart.juniorstart.security.UserPrincipal;
import com.juniorstart.juniorstart.service.ProjectCommentService;
import com.juniorstart.juniorstart.util.AppConstants;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/project/comment")
public class ProjectCommentController {

    public ProjectCommentController(ProjectCommentService projectCommentService) {
        this.projectCommentService = projectCommentService;
    }

    private final ProjectCommentService projectCommentService;

    @GetMapping
    public PagedResponse<ProjectCommentResponse> findAll(@CurrentUser UserPrincipal currentUser,  @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                         @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size)  {
        return projectCommentService.findAllComment(currentUser, page, size);
    }

    @GetMapping("/{commentId}")
    public ProjectCommentResponse findById(@PathVariable Long commentId) {
        return projectCommentService.findById(commentId);
    }

    @PostMapping("/{projectId}")
    @PreAuthorize("hasRole('USER')")
    public ProjectCommentResponse createComment(@CurrentUser UserPrincipal currentUser, @RequestBody @Valid CommentRequest commentRequest, @PathVariable Long projectId) {
        return projectCommentService.createComment(commentRequest, currentUser, projectId);
    }

    @DeleteMapping("/{commentId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> delete(@CurrentUser UserPrincipal currentUser, @PathVariable Long commentId) {
        return projectCommentService.deleteComment(commentId, currentUser);
    }

    @GetMapping("/user/{userId}")
    public PagedResponse<ProjectCommentResponse> findByCreatedBy(@PathVariable Long userId, @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                              @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
        return projectCommentService.findByCreatedBy(userId, page, size);
    }

    @GetMapping("/project/{projectId}")
    public PagedResponse<ProjectCommentResponse> findByProjectId(@PathVariable Long projectId, @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                                 @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
        return projectCommentService.findByProjectId(projectId, page, size);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<?> updatedComment(@CurrentUser UserPrincipal currentUser, @PathVariable Long commentId,  @RequestBody @Valid CommentUpdateRequest commentUpdateRequest) {
        return projectCommentService.updateComment(currentUser, commentId, commentUpdateRequest);
    }

}
