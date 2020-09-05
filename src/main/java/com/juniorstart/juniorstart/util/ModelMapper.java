package com.juniorstart.juniorstart.util;

import com.juniorstart.juniorstart.model.*;
import com.juniorstart.juniorstart.payload.*;

import java.util.List;
import java.util.stream.Collectors;


/** Util class to map a model class to a model responses class.
 * @author Grzegorz Szczęsny
 * @version 1.0
 * @since 1.0
 */
public class ModelMapper {



    /** Map Project's object to a ProjectResponse class, which is send to response.
     * @param project The object that represents a project's credentials.
     * @param user The object that represents a user's credentials.
     * @param userProjectLike The like's amount on project.
     * @return a {@link ProjectResponse} that is send to a frontend.
     */
    public static ProjectResponse mapProjectToProjectResponse(Project project, User user, long userProjectLike) {

        ProjectResponse projectResponse = new ProjectResponse();
        projectResponse.setId(project.getId());
        projectResponse.setTitle(project.getTitle());
        projectResponse.setDescription(project.getDescription());
        projectResponse.setBody(project.getBody());
        projectResponse.setNumberOfSeats(project.getNumberOfSeats());
        projectResponse.setRepository(project.getRepository());
        projectResponse.setName(project.getName());
        projectResponse.setLikes(userProjectLike);

             List<ProjectTodoResponse> projectTodoResponses = project.getProjectTodos().stream().map(projectTodo -> {
            return ModelMapper.mapProjectTodoToProjectTodoResponse(projectTodo, projectTodo.getUser());
        }).collect(Collectors.toList());

             List<TechnologyResponse> technologyResponses = project.getTechnologies().stream().map(technology -> {
                 return ModelMapper.mapTechnologyToTechnologyResponse(technology);
             }).collect(Collectors.toList());


        List<UserSummary> team_members = project.getTeam_members().stream().map(member -> {
            return UserSummary.builder().id(member.getPublicId()).name(member.getName())
                    .email(member.getEmail()).build();
        }).collect(Collectors.toList());

        List<ProjectCommentResponse> comments = project.getProjectComments().stream().map(projectComment -> {
            return ModelMapper.mapProjectCommentToProjectCommentResponse(projectComment, user);
        }).collect(Collectors.toList());


        UserSummary userSummary = UserSummary.builder().id(user.getPublicId()).email(user.getEmail()).name(user.getName()).build();
        projectResponse.setCreator(userSummary);
        projectResponse.setProjectTodos(projectTodoResponses);
        projectResponse.setTeamMembers(team_members);
        projectResponse.setTechnologies(technologyResponses);
        projectResponse.setProjectComments(comments);

        return projectResponse;
    }

    /**
     * Map ProjectTodo's object to a {@link ProjectTodoResponse}, which is send to response.
     * @param projectTodo The object that represents a project's credentials.
     * @param user The object that represents a user's credentials.
     * @return a {@link ProjectTodoResponse} that is send to a frontend.+
     */
    public static ProjectTodoResponse mapProjectTodoToProjectTodoResponse(ProjectTodo projectTodo , User user) {
        ProjectTodoResponse projectTodoResponse = new ProjectTodoResponse();

        projectTodoResponse.setId(projectTodo.getId());
        UserSummary userSummary = UserSummary.builder().id(user.getPublicId()).email(user.getEmail()).name(user.getName()).build();
        projectTodoResponse.setCreator(userSummary);
        projectTodoResponse.setProject_id(projectTodo.getId());
        projectTodoResponse.setTitle(projectTodo.getTitle());

        return projectTodoResponse;
    }

    /**
     * Map Technology's object to a {@link Technology}, which is send to response.
     * @param technology The object that represents a technology's credentials.
     * @return a {@link TechnologyResponse} that is send a frontend.
     */
    public static TechnologyResponse mapTechnologyToTechnologyResponse(Technology technology) {
        TechnologyResponse technologyResponse = new TechnologyResponse();
        technologyResponse.setDescription(technology.getDescription());
        technologyResponse.setTitle(technology.getTitle());
        technologyResponse.setId(technology.getId());
        technologyResponse.setTechnologyType(technology.getTechnologyType().toString());

        return technologyResponse;
    }

    /**
     * Map ProjectComment's object to a {@link ProjectComment}, which is send to response.
     * @param projectComment The object that represents a technology's credentials.
     * @param creator The object that represents a user's credentials.
     * @return a {@link ProjectCommentResponse} that is send to a frontend.
     */
    public static ProjectCommentResponse mapProjectCommentToProjectCommentResponse(ProjectComment projectComment, User creator) {
        ProjectCommentResponse projectCommentResponse = new ProjectCommentResponse();
        projectCommentResponse.setId(projectComment.getId());
        projectCommentResponse.setBody(projectComment.getBody());
        projectCommentResponse.setCreationDateTime(projectComment.getCreatedAt());
        projectCommentResponse.setProjectId(projectComment.getProject().getId());
        UserSummary userSummary = UserSummary.builder().id(creator.getPublicId()).email(creator.getEmail()).name(creator.getName()).build();
        projectCommentResponse.setCreatedBy(userSummary);

        return projectCommentResponse;
    }


}
