package com.juniorstart.juniorstart.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.juniorstart.juniorstart.generation.UserIdGenerator;
import com.juniorstart.juniorstart.model.audit.UserStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "email")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User  {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private UUID privateId;

    @NaturalId
    private Long publicId = UserIdGenerator.generateId();

    private String name;

    private Integer age;

    private boolean hiddenFromSearch;

    @Email
    @Column(nullable = false)
    private String email;

    private String imageUrl;

    @Column(nullable = false)
    private Boolean emailVerified = false;

    @JsonIgnore
    private String password;

    @NotNull
    @Enumerated(EnumType.STRING)
    private AuthProvider provider;

    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;

    private String providerId;

    @OneToMany(
            fetch =  FetchType.LAZY,
            cascade = CascadeType.ALL,
            targetEntity = Project.class,
            mappedBy = "user")
    private Set<Project> projects = new HashSet<>();
}
