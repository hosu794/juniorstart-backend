package com.juniorstart.juniorstart.model;

import com.juniorstart.juniorstart.model.audit.UserDateAudit;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "projects", uniqueConstraints = {
        @UniqueConstraint(columnNames = "name")
})
@Getter
@Setter
@NoArgsConstructor
public class Project  extends UserDateAudit {

    @Id
    @Column(name = "project_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    @NotEmpty(message = "Please provide a name")
    private String name;

    @Column(name = "title", nullable = false)
    @NotEmpty(message = "Please provide a title")
    private String title;

    @Column(name = "description", nullable = false)
    @NotEmpty(message = "Please provide a description")
    private String description;

    @Column(name = "body", columnDefinition = "TEXT", nullable = false)
    @NotEmpty(message = "Please provide a body")
    private String body;

    @Column(name = "number_of_seats", nullable = false)
    @NotEmpty(message = "Please provide a number of seats")
    private long numberOfSeats;

    @Column(name = "repository", nullable = false)
    @NotEmpty(message = "Please provide a project repository")
    private String repository;

    @Column(name = "recruiting")
    private Boolean recruiting = false;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "project_technologies",
            joinColumns =  @JoinColumn(name = "project_id"), inverseJoinColumns = @JoinColumn(name = "technology_id"))
    private Set<Technologies> technologies = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "team_members", joinColumns = @JoinColumn(name = "project_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> team_members = new HashSet<>();

    @OneToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "user_id")
    private User mentor;


}
