package com.juniorstart.juniorstart.model;


import lombok.*;

import javax.persistence.*;

@Data
@EqualsAndHashCode(of = "id")
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
@Builder
public class EmploymentHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String companyName;

    private String tasksAtWork;

    @Column(columnDefinition = "DATE")
    private java.sql.Date dateStartOfEmployment;

    @Column(columnDefinition = "DATE")
    private java.sql.Date dateEndOfEmployment;

    @Column(columnDefinition = "boolean default false")
    private Boolean isCurrentEmployment;

    @ManyToOne
    private UserProfile userProfile;

}





