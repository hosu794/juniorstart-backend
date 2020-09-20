package com.juniorstart.juniorstart.mapper;

import com.juniorstart.juniorstart.model.Project;
import com.juniorstart.juniorstart.model.dto.ProjectDto;
import jakarta.validation.constraints.NotBlank;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/** Represents an user service.
 * @author noboseki
 * @version 1.0
 * @since 1.0
 */
@Component
public class ProjectMapper {

    /** Method to  dto object to entity.
     * @param dto object to map.
     * @return Project object mapped form ProjectDto.
     */
    public Project mapToEntity(@NotBlank ProjectDto dto) {
        return Project.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .description(dto.getDescription())
                .progress(dto.getProgress())
                .url(dto.getUrl())
                .user(dto.getUser()).build();
    }

    /** Method to entity object to dto.
     * @param project object to map.
     * @return ProjectDto object mapped form Project.
     */
    public ProjectDto mapToDto(@NotBlank Project project) {
        return ProjectDto.builder()
                .id(project.getId())
                .title(project.getTitle())
                .description(project.getDescription())
                .progress(project.getProgress())
                .url(project.getUrl())
                .user(project.getUser()).build();
    }

    /** Method to map list entities object to list dtos.
     * @param projects list of objects to map.
     * @return List of ProjectDto object mapped form list of Projects.
     */
    public List<ProjectDto> mapToDtoList(@NotBlank List<Project> projects) {
        return projects.stream()
                .map(this::mapToDto)
                .collect( Collectors.toList());
    }
}
