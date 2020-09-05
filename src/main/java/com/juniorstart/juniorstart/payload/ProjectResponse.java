package com.juniorstart.juniorstart.payload;


import com.juniorstart.juniorstart.model.Technology;
import lombok.*;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectResponse {

    private long id;
    private String name;
    private String title;
    private String description;
    private String body;
    private long numberOfSeats;
    private String repository;
    private boolean recruiting;
    private long likes;
    private List<TechnologyResponse> technologies;
    private List<ProjectTodoResponse> projectTodos;
    private List<UserSummary> teamMembers;
    private UserSummary mentor;
    private UserSummary creator;
    private Collection<ProjectCommentResponse> projectComments;


}
