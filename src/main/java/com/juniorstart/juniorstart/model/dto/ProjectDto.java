package com.juniorstart.juniorstart.model.dto;

import com.juniorstart.juniorstart.model.User;
import com.juniorstart.juniorstart.model.audit.ProjectStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProjectDto {

    private long id;
    private String title;
    private ProjectStatus progress;
    private String description;
    private String url;
    private User user;
}
