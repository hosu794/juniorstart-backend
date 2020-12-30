package com.juniorstart.juniorstart.payload;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectResponse {

    private UUID id;
    private String name;
    private String title;
    private String description;
    private String body;
    private long numberOfSeats;
    private String repository;
    private boolean recruiting;
    private List<UserSummary> teamMembers;
    private UserSummary mentor;
    private UserSummary creator;
}
