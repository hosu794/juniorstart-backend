package com.juniorstart.juniorstart.model;

import lombok.*;
import javax.persistence.*;
import java.util.HashSet;
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

    private String technologyName;

    @ManyToMany(mappedBy="userTechnology")
    private Set<UserProfile> usersProfile = new HashSet<>();



    @Override
    public String toString() {
        return "UserTechnology{" +
                "id=" + id +
                ", technologyName='" + technologyName + '\'' +
                ", usersProfile=" + usersProfile +
                '}';
    }


}


