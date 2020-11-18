package com.juniorstart.juniorstart.model;

import com.juniorstart.juniorstart.model.audit.UserDateAudit;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = "title")
})
public class Technologies extends UserDateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private TechnologyType technologyType;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Singular
    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            },
            mappedBy = "technologies")
    private Set<Project> projects = new HashSet<>();
}
