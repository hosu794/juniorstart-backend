package com.juniorstart.juniorstart.model;

import lombok.*;
import javax.persistence.Entity;
import javax.persistence.*;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "privateId")
@Entity
@Table(name="users_profile")
@Builder
public class UserProfile {
    @Id
    private UUID privateId;

    @OneToOne
    @MapsId
    private User user;

    @OneToMany(mappedBy = "userProfile", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @Builder.Default
    private List<EmploymentHistory> employmentsHistory = new ArrayList<>();

    private String selfDescription;

    private String careerGoals;

    @Lob
    private byte[] userAvatar;

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
    public void addUserManyTechnology(Set<UserTechnology> technology){
        for(UserTechnology userTechnology: technology) {
            this.userTechnology.add(userTechnology);
            userTechnology.getUsersProfile().add(this);
        }
    }

    public void addEmploymentHistory(EmploymentHistory employmentHistory) {
        this.employmentsHistory.add(employmentHistory);
        employmentHistory.setUserProfile(this);
    }

    public UserProfileDto toUserProfileDto() {
        return new UserProfileDto(
                this.user,
                this.userRole,
                this.userTechnology,
                this.employmentsHistory,
                this.selfDescription,
                this.careerGoals,
                this.userAvatar);
    }

    @Data
    @AllArgsConstructor
    public static class UserProfileDto {
        private User user;
        private UserRole userRole;
        private Set<UserTechnology> userTechnology;
        private List<EmploymentHistory> employmentsHistory;
        private String selfDescription;
        private String careerGoals;
        private byte[] userAvatar;
    }
}
