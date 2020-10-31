package com.juniorstart.juniorstart.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
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

    private boolean isCurrentEmployment;

    @ManyToOne
    private UserProfile userProfile;

}





