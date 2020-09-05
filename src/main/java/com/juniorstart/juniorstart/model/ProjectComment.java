package com.juniorstart.juniorstart.model;

import com.juniorstart.juniorstart.model.audit.UserDateAudit;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectComment extends UserDateAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_comment_id")
    private Long id;

    @Column(name = "body", columnDefinition = "TEXT", nullable = false)
    @NotEmpty(message = "Please provide a body")
    private String body;

    @ManyToOne
    @JoinColumn(name = "project_id", referencedColumnName = "project_id", nullable = false)
    @NotNull
    private Project project;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    @NotNull
    private User user;


}
