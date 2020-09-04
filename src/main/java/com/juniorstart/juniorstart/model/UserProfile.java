package com.juniorstart.juniorstart.model;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name="users_profile")
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    //@MapsId("publicId")
    private User user;


    @Enumerated(EnumType.STRING)
    private ListUserRole userRole;


    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name="join_user_technology",
    joinColumns = @JoinColumn(name="user_id"),
    inverseJoinColumns = @JoinColumn(name="technology_id"))
    private Set<UserTechnology> userTechnology = new HashSet<>();


    public void addUserTechnology(UserTechnology Technology){
        userTechnology.add(Technology);
        Technology.getUserProfile().add(this);
    }
    public void removeUserTechnology(UserTechnology technology){
        userTechnology.remove(technology);
        technology.getUserProfile().remove(this);
    }


    @Override
    public String toString() {
        return "UserProfile{" +
                "id=" + id +
                ", user=" + user +
                ", userRole=" + userRole +
                ", userTechnology=" + userTechnology+
                '}';
    }
}
