package com.juniorstart.juniorstart.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.juniorstart.juniorstart.generation.UserIdGenerator;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "email")
})
@Data
//@EqualsAndHashCode(exclude="userProfile")
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

    private String providerId;


    @OneToOne(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private UserProfile userProfile;


}
