package com.juniorstart.juniorstart.service;

import com.juniorstart.juniorstart.exception.BadRequestException;
import com.juniorstart.juniorstart.exception.ResourceNotFoundException;
import com.juniorstart.juniorstart.model.Project;
import com.juniorstart.juniorstart.model.ProjectComment;
import com.juniorstart.juniorstart.model.User;
import com.juniorstart.juniorstart.payload.*;
import com.juniorstart.juniorstart.repository.ProjectCommentRepository;
import com.juniorstart.juniorstart.repository.ProjectLikeRepository;
import com.juniorstart.juniorstart.repository.ProjectRepository;
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


/** Represents an comment service which manipulate project's comments data. 20-09-2020.
 * @author Grzegorz Szczęsny
 * @version 1.0
 * @since 1.0
 */
@Service
@Slf4j
public class ProjectCommentService {

    public ProjectCommentService(ProjectCommentRepository projectCommentRepository, UserDao userDao, ProjectRepository projectRepository, ProjectLikeRepository projectLikeRepository) {
        this.projectCommentRepository = projectCommentRepository;
        this.userDao = userDao;
        this.projectRepository = projectRepository;
        this.projectLikeRepository = projectLikeRepository;
    }

    final private ProjectCommentRepository projectCommentRepository;
    final private UserDao userDao;
    final private ProjectRepository projectRepository;
    final private ProjectLikeRepository projectLikeRepository;

    /** Create a new comment for a project.
     * @param projectId A project's identification number.
     * @param currentUser The current user credentials.
     * @param commentRequest The comment’s credentials.
     * @return a created comment
     */
    public ProjectCommentResponse createComment(CommentRequest commentRequest, UserPrincipal currentUser, Long projectId) {

        User currentLoggedUser = userDao.findById(currentUser.getId()).orElseThrow(() -> new ResourceNotFoundException("User", "userId", currentUser.getId()));

        Project project = projectRepository.findById(projectId).orElseThrow(() -> new ResourceNotFoundException("Project", "projectId", projectId));

        User projectCreator = userDao.findByPublicId(project.getCreatedBy()).orElseThrow(() -> new ResourceNotFoundException("User" , "userId" , project.getCreatedBy()));

        long likeCountOfProjectCreator = projectLikeRepository.countByProjectId(projectId);
        ProjectResponse projectResponse = ModelMapper.mapProjectToProjectResponse(project, projectCreator, likeCountOfProjectCreator);

        ProjectComment projectComment = new ProjectComment();
        projectComment.setBody(commentRequest.getBody());
        projectComment.setProject(project);
        projectComment.setUser(currentLoggedUser);

        ProjectComment createdComment = projectCommentRepository.save(projectComment);

        return ModelMapper.mapProjectCommentToProjectCommentResponse(projectComment, projectCreator);

    }

    /**
     * Get comment by identification number.
     * @param commentId A project's identification number
     * @return a {@link ProjectCommentResponse} with comment's credentials
     */
    public ProjectCommentResponse findById(Long commentId) {
        ProjectComment projectComment = projectCommentRepository.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("ProjectComment", "commentId", commentId));
        User user = userDao.findByPublicId(projectComment.getCreatedBy()).orElseThrow(() -> new ResourceNotFoundException("User", "userId", projectComment.getCreatedBy()));

        return ModelMapper.mapProjectCommentToProjectCommentResponse(projectComment, user);
    }

    /**
     * Delete a comment by identification number.
     * @param commentId A comment's identification number that we use to delete it.
     * @param currentUser A current user's credentials.
     * @return A {@link ApiResponse} with the message.
     */
    public ResponseEntity<?> deleteComment(Long commentId, UserPrincipal currentUser) {
        ProjectComment projectComment = projectCommentRepository.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("Comment", "commentId", commentId));
        User user = userDao.findById(currentUser.getId()).orElseThrow(() -> new ResourceNotFoundException("User", "userId", currentUser.getId()));

        long creationId = user.getPublicId();
        long commentCreationId = projectComment.getCreatedBy();
        if(creationId == commentCreationId) {
            projectCommentRepository.delete(projectComment);
            return  ResponseEntity.ok(new ApiResponse(true, "Comment has been deleted successfully"));
        } else {
            throw new BadRequestException("Sorry you not created this comment");
        }
    }

    /**
     * Update a comment
     * @param currentUser A current user's credentials
     * @param commentId A comment's identification number
     * @param commentUpdateRequest A request that contains a new credentials to update.
     * @return A {@link ApiResponse} with the message.
     */
    public ResponseEntity<?> updateComment(UserPrincipal currentUser, Long commentId, CommentUpdateRequest commentUpdateRequest) {
        User user = userDao.findById(currentUser.getId()).orElseThrow(() -> new ResourceNotFoundException("User", "userId", currentUser.getId()));

        ProjectComment comment = projectCommentRepository.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("ProjectComment", "commentId", commentId));
        long creationId = user.getPublicId();
        long commentCreationId = comment.getCreatedBy();

        if(creationId == commentCreationId) {
            comment.setBody(commentUpdateRequest.getBody());
            projectCommentRepository.save(comment);
            return  ResponseEntity.ok(new ApiResponse(true, "Comment has been updated successfully"));
        } else {
            throw new BadRequestException("Sorry you not created this comment");
        }

    }

    /**
     * Get all project's credentials
     * @param currentUser A current user's credentials
     * @param page A page number
     * @param size A page size
     * @return {@link PagedResponse} with a {@link ProjectCommentResponse}
     */
    public PagedResponse<ProjectCommentResponse> findAllComment(UserPrincipal currentUser, int page, int size) {
        ValidatePageUtil.validatePageNumberAndSize(page, size);
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");

        Page<ProjectComment> projectComment = projectCommentRepository.findAll(pageable);

        if(projectComment.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), projectComment.getNumber(), projectComment.getSize(), projectComment.getTotalElements(), projectComment.getTotalPages(), projectComment.isLast());
        }

        List<ProjectCommentResponse> projectCommentResponses = projectComment.map(comment -> {

            Project project = comment.getProject();
            User creator = userDao.findByPublicId(project.getCreatedBy()).orElseThrow(() -> new ResourceNotFoundException("User", "userId", comment.getCreatedBy()));
            long likeCountOfProject = projectLikeRepository.countByProjectId(project.getId());

            ProjectResponse projectResponse = ModelMapper.mapProjectToProjectResponse(project,creator, likeCountOfProject);

            return ModelMapper.mapProjectCommentToProjectCommentResponse(comment, creator);

        }).getContent();

        return new PagedResponse<>(projectCommentResponses, projectComment.getNumber(), projectComment.getSize(), projectComment.getTotalElements(), projectComment.getTotalPages(), projectComment.isLast());

    }

    /**
     * Find all comments by project's identification number.
     * @param projectId A project's identification
     * @param page A page number.
     * @param size A page size
     * @return {@link PagedResponse} with a {@link ProjectCommentResponse}
     */
    public PagedResponse<ProjectCommentResponse> findByProjectId(Long projectId, int page, int size) {
        ValidatePageUtil.validatePageNumberAndSize(page, size);

        Project project = projectRepository.findById(projectId).orElseThrow(() -> new ResourceNotFoundException("User", "userId", projectId));

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        Page<ProjectComment> projectComments = projectCommentRepository.findByProjectId(projectId, pageable);

        if(projectComments.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), projectComments.getNumber(), projectComments.getSize(), projectComments.getTotalElements(), projectComments.getTotalPages(), projectComments.isLast());
        }

        User projectCreator = userDao.findByPublicId(project.getCreatedBy()).orElseThrow(() -> new ResourceNotFoundException("User", "userId", project.getCreatedBy()));

        long likeCountOfProject = projectLikeRepository.countByProjectId(projectId);

        ProjectResponse projectResponse = ModelMapper.mapProjectToProjectResponse(project, projectCreator, likeCountOfProject);

        List<ProjectCommentResponse> projectCommentResponses = projectComments.map(projectComment -> ModelMapper.mapProjectCommentToProjectCommentResponse(projectComment, projectCreator)).getContent();

        return new PagedResponse<>(projectCommentResponses, projectComments.getNumber(), projectComments.getSize(), projectComments.getTotalElements(), projectComments.getTotalPages(), projectComments.isLast());

    }

    /**
     * Find all comments by a user's identification number.
     * @param userId The user's identification number.
     * @param page A page number.
     * @param size A page size.
     * @return {@link PagedResponse} with a {@link ProjectCommentResponse}
     */
    public PagedResponse<ProjectCommentResponse> findByCreatedBy(Long userId, int page, int size) {
        ValidatePageUtil.validatePageNumberAndSize(page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        Page<ProjectComment> projectComments = projectCommentRepository.findByCreatedBy(userId, pageable);

        User creator = userDao.findByPublicId(userId).orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));
        if(projectComments.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), projectComments.getNumber(), projectComments.getSize(), projectComments.getTotalElements(), projectComments.getTotalPages(), projectComments.isLast());
        }

        List<ProjectCommentResponse> projectCommentResponses = projectComments.map(projectComment -> {

            Project project = projectComment.getProject();
            User creatorOfStory = userDao.findByPublicId(project.getCreatedBy()).orElseThrow(() -> new ResourceNotFoundException("User", "userId", project.getCreatedBy()));

            long likeCountOfProject = projectLikeRepository.countByProjectId(project.getId());

            ProjectResponse projectResponse = ModelMapper.mapProjectToProjectResponse(project, creatorOfStory, likeCountOfProject);

            return ModelMapper.mapProjectCommentToProjectCommentResponse(projectComment, creator);

        }).getContent();

        return new PagedResponse<>(projectCommentResponses, projectComments.getNumber(), projectComments.getSize(), projectComments.getTotalElements(), projectComments.getTotalPages(), projectComments.isLast());
    }



}
