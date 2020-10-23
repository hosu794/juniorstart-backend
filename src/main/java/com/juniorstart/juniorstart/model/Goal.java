package com.juniorstart.juniorstart.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import javax.persistence.*;
import java.sql.Date;
import java.util.UUID;

@Builder
@Getter
@Setter
@Entity
@Table(name = "goal")
@AllArgsConstructor
@NoArgsConstructor
public class Goal {

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false,
            length = 60)
    private String name;

    @Column(nullable = false)
    private GoalType goalType;

    private String description;

    @Builder.Default
    @Column(nullable = false)
    boolean isEnded = false;

    @Column(nullable = false)
    private Date endDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Override
    public String toString() {
        return "Goal{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", goalType=" + goalType +
                ", description='" + description + '\'' +
                ", isEnded=" + isEnded +
                ", endedDate=" + endDate +
                '}';
    }

    @ToString
    @Builder
    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GoalDto {

        @JsonIgnore
        private UUID id;

        @Max(60)
        private String name;

        @NotNull
        private GoalType goalType;

        private String description;

        @Builder.Default
        boolean isEnded = false;

        @NotNull
        private Date endDate;

    }
}
