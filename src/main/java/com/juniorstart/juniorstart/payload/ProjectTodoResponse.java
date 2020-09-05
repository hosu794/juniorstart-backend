package com.juniorstart.juniorstart.payload;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProjectTodoResponse {

    private Long id;

    private String title;

    private UserSummary creator;

    private Long project_id;
}
