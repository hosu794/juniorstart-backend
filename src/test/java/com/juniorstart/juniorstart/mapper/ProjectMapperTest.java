package com.juniorstart.juniorstart.mapper;

import com.juniorstart.juniorstart.model.Project;
import com.juniorstart.juniorstart.model.User;
import com.juniorstart.juniorstart.model.audit.ProjectStatus;
import com.juniorstart.juniorstart.model.dto.ProjectDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/** Test of ProjectMapper methods.
 * @author noboseki
 * @version 1.0
 * @since 1.0
 */
public class ProjectMapperTest {
    ProjectMapper projectMapper = new ProjectMapper();
    Project project;
    ProjectDto projectDto;
    List<Project> projects = new ArrayList<>();

    @BeforeEach
    void setUp() {
        project = Project.builder()
                .id(1L)
                .title("Test title")
                .description("Test description")
                .progress(ProjectStatus.IN_PROGRESS)
                .url("Test url")
                .user(new User()).build();

        projectDto = ProjectDto.builder()
                .id(2L)
                .title("TestDto title")
                .description("TestDto description")
                .progress(ProjectStatus.COMPLETE)
                .url("TestDto url")
                .user(new User()).build();
    }

    @Test
    @DisplayName("Test map to entity")
    void mapToEntity() {
        //When
        Project mapped = projectMapper.mapToEntity(projectDto);

        //Then
        assertEquals(2L, mapped.getId());
        assertEquals("TestDto title", mapped.getTitle());
        assertEquals("TestDto description", mapped.getDescription());
        assertEquals(ProjectStatus.COMPLETE, mapped.getProgress());
        assertEquals("TestDto url", mapped.getUrl());
        assertNotNull(mapped.getUser());
    }

    @Test
    @DisplayName("Test map to dto")
    void mapToDto() {
        //When
        ProjectDto mapped = projectMapper.mapToDto(project);

        //Then
        assertEquals(1L, mapped.getId());
        assertEquals("Test title", mapped.getTitle());
        assertEquals("Test description", mapped.getDescription());
        assertEquals(ProjectStatus.IN_PROGRESS, mapped.getProgress());
        assertEquals("Test url", mapped.getUrl());
        assertNotNull(mapped.getUser());
    }

    @Test
    @DisplayName("Test map to dto list")
    void mapToDtoList() {
        //Given
        projects.add(project);
        projects.add(Project.builder()
                .id(2L)
                .title("TestDto title")
                .description("TestDto description")
                .progress(ProjectStatus.COMPLETE)
                .url("TestDto url")
                .user(new User()).build());

        //When
        List<ProjectDto> mappedList = projectMapper.mapToDtoList(projects);

        //Then
        assertEquals(2,mappedList.size());
        assertEquals(1L, mappedList.get(0).getId());
        assertEquals(2L, mappedList.get(1).getId());
    }
}