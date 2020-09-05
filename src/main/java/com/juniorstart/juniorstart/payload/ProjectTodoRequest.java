package com.juniorstart.juniorstart.payload;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ProjectTodoRequest {

    private String title;
    long projectId;

}
