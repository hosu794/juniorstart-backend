package com.juniorstart.juniorstart.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.juniorstart.juniorstart.model.audit.ProjectStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "projects")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private long id;

    @Column(nullable = false, length = 50)
    private String title;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private ProjectStatus progress;

    private String description;

    private String url;

    @NotNull
    @ManyToOne(
            fetch = FetchType.EAGER,
            cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH},
            targetEntity = User.class)
    @JoinColumn(name = "user_id")
    private User user;
}
