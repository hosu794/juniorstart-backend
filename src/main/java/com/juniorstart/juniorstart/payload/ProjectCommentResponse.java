package com.juniorstart.juniorstart.payload;

import lombok.*;

import java.time.Instant;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProjectCommentResponse {

    private Long id;
    private String body;
    private Long projectId;
    private UserSummary createdBy;
    private Instant creationDateTime;


}
