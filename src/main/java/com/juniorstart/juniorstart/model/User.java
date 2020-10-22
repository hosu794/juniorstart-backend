package com.juniorstart.juniorstart.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.juniorstart.juniorstart.generation.UserIdGenerator;
import com.juniorstart.juniorstart.model.audit.UserStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.NaturalId;
import org.springframework.stereotype.Component;

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

    @Builder.Default
    private boolean hiddenFromSearch = false;

    @Email
    @Column(nullable = false)
    private String email;

    private String imageUrl;

    @Builder.Default
    @Column(nullable = false)
    private Boolean emailVerified = false;

    @JsonIgnore
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AuthProvider provider;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus userStatus;

    private String providerId;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "offerCreator",cascade =  CascadeType.ALL)
    private Set<JobOffer> jobOffers = new HashSet<>();

    @OneToMany(
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            mappedBy = "user")
    private Set<Goal> goals = new HashSet<>();
}
