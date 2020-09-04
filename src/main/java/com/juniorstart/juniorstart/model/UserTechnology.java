package com.juniorstart.juniorstart.model;


import com.juniorstart.juniorstart.model.UserProfile;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "users_technology")
public class UserTechnology {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String technology;

    @ManyToMany(mappedBy="userTechnology")
    private Set<UserProfile> userProfile = new HashSet<>();



}
