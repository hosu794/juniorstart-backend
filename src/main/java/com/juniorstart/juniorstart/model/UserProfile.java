package com.juniorstart.juniorstart.model;

import lombok.*;
import javax.persistence.Entity;
import javax.persistence.*;
import java.util.*;

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

    public void addUserManyTechnology(Set<UserTechnology> technology){

        /*
        Iterator value = technology.iterator();
        while (value.hasNext()) {
            System.out.println(value.next());
            this.userTechnology.add(value.next().getClass());
            technology.getUsersProfile().add(this);
        }

         */
        for(UserTechnology userTechnology: technology) {
            System.out.println(userTechnology);
            System.out.println("111111111111111111111111");
            this.userTechnology.add(userTechnology);
            userTechnology.getUsersProfile().add(this);
        }
        /*
        for(int i=0; i<technology.size() ;i++)
            System.out.println(i);
        this.userTechnology.add(technology.getClass().);
         */

      //  this.userTechnology.add(technology);
       // technology.getUsersProfile().add(this);

    }

/*
    @Override
    public String toString() {
        return "UserProfile{" +
                "privateId=" + privateId +
                ", user=" + user +
                ", userRole=" + userRole +
                ", userTechnology=" + userTechnology +
                '}';
    }
 */

}
