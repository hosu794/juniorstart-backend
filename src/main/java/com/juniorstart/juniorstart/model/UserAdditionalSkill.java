package com.juniorstart.juniorstart.model;

import lombok.*;

import javax.persistence.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude="userTechnology")
@Entity
@Table(name = "additional_skills")
public class UserAdditionalSkill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true)
    private String additionalSkill;

    @ManyToOne
    @JoinColumn(name="technology_id")
    private UserTechnology userTechnology;

}
