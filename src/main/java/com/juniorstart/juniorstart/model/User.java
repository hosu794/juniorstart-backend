package com.juniorstart.juniorstart.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.juniorstart.juniorstart.generation.UserIdGenerator;
import com.juniorstart.juniorstart.model.audit.DateAudit;
import com.juniorstart.juniorstart.model.audit.UserDateAudit;
import com.juniorstart.juniorstart.model.audit.UserStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;
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
public class User extends DateAudit {

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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "offerCreator",cascade =  CascadeType.ALL)
    private Set<JobOffer> jobOffers = new HashSet<>();
}
