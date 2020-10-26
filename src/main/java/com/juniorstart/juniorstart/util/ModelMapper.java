package com.juniorstart.juniorstart.util;


import com.juniorstart.juniorstart.model.Project;
import com.juniorstart.juniorstart.model.User;
import com.juniorstart.juniorstart.payload.ProjectResponse;

/** Util class to map a model class to a model responses class.
 * @author Grzegorz Szczęsny
 * @version 1.0
 * @since 1.0
 */
public class ModelMapper {

    /** Map Project's object to a ProjectResponse class, which is send to response.
     * @param project The object that represents a project's credentials.
     * @param user The object that represents a user's credentials.
     * @return a {@link ProjectResponse} that is send to a frontend.
     */
    public static ProjectResponse mapProjectToProjectResponse(Project project, User user) {

        ProjectResponse projectResponse = new ProjectResponse();
        projectResponse.setId(project.getId());
        projectResponse.setTitle(project.getTitle());
        projectResponse.setDescription(project.getDescription());
        projectResponse.setBody(project.getBody());
        projectResponse.setNumberOfSeats(project.getNumberOfSeats());
        projectResponse.setRepository(project.getRepository());
        projectResponse.setName(project.getName());

        return projectResponse;
    }
}
