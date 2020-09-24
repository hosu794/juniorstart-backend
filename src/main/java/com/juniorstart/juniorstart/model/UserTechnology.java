package com.juniorstart.juniorstart.model;

import lombok.*;
import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude="usersProfile")
@Entity
@Table(name = "technologies")
public class UserTechnology {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String technologyName;

    @ManyToMany(mappedBy="userTechnology")
    private Set<UserProfile> usersProfile = new HashSet<>();


    @OneToMany(mappedBy="userTechnology",
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private Set<UserAdditionalSkill> userAdditionalSkills;


    public void addAdditionalSKill(UserAdditionalSkill userAdditionalSkill) {
        this.userAdditionalSkills.add(userAdditionalSkill);
    }

    public void addManyAdditionalSkill(Set<UserAdditionalSkill> userAdditionalSkill) {
        for(UserAdditionalSkill userSkill: userAdditionalSkill){
            this.userAdditionalSkills.add(userSkill);
            userSkill.setUserTechnology(this);
        }
    }

    @Override
    public String toString() {
        return "UserTechnology{" +
                "id=" + id +
                ", technologyName='" + technologyName + '\'' +
                ", usersProfile=" + usersProfile +
                '}';
    }


}


