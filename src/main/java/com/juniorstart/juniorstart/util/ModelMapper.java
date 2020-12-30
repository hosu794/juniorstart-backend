package com.juniorstart.juniorstart.util;


import com.juniorstart.juniorstart.model.Project;
import com.juniorstart.juniorstart.model.User;
import com.juniorstart.juniorstart.payload.ProjectResponse;
import com.juniorstart.juniorstart.payload.UserSummary;

/** Util class to map a model class to a model responses class.
 * @author Grzegorz Szczęsny
 * @version 1.0
 * @since 1.0
 */
public class ModelMapper {

    /** Map Project's object to a ProjectResponse class, which is send to response.
     * @param project The object that represents a project's credentials.
     * @return a {@link ProjectResponse} that is send to a frontend.
     */
    public static ProjectResponse mapProjectToProjectResponse(Project project, User creator) {

        UserSummary userSummary = UserSummary.builder()
                .email(creator.getEmail())
                .id(creator.getPublicId())
                .name(creator.getName())
                .build();

        return ProjectResponse.builder()
                .id(project.getId())
                .title(project.getTitle())
                .description(project.getDescription())
                .body(project.getBody())
                .numberOfSeats(project.getNumberOfSeats())
                .repository(project.getRepository())
                .name(project.getName())
                .creator(userSummary)
                .build();
    }
}
