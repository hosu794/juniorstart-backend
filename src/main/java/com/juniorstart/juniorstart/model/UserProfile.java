package com.juniorstart.juniorstart.model;

import lombok.*;
import javax.persistence.Entity;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude="userTechnology")
@Entity
@Table(name="users_profile")
@Builder
public class UserProfile {
    @Id
    private UUID privateId;

    @OneToOne
    @MapsId
    private User user;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name="join_user_technology",
    joinColumns = {@JoinColumn(name="user_id")},
    inverseJoinColumns = {@JoinColumn(name="technology_id")})
    private Set<UserTechnology> userTechnology = new HashSet<>();

    public void addUserTechnology(UserTechnology technology){
        this.userTechnology.add(technology);
        technology.getUsersProfile().add(this);
    }
    public void removeUserTechnology(UserTechnology technology){
        userTechnology.remove(technology);
        technology.getUsersProfile().remove(this);
    }
}
