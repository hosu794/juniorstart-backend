package com.juniorstart.juniorstart.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/** Entity for user goal.
 * @author Noboseki
 * @version 1.0
 * @since 1.0
 */
@Entity
@Table(name = "goal")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Goal {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private UUID privateId;

    @Column(length = 40,nullable = false)
    private String title;

    private String description;

    @Column(nullable = false)
    private Date endDate;

    @Column(nullable = false)
    private Boolean done = false;

    @Column(nullable = false)
    @ManyToOne(
            fetch = FetchType.EAGER,
            cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH },
            targetEntity = User.class)
    @JoinColumn(name = "user_id")
    private User user;

    public Goal mapToEntity(GoalDto goalDto) {
        return Goal.builder()
                .privateId(goalDto.privateId)
                .title(goalDto.title)
                .description(goalDto.description)
                .endDate(goalDto.getEndDate())
                .done(goalDto.getDone())
                .user(goalDto.getUser()).build();
    }

    public GoalDto mapToDto(Goal goal) {
        return GoalDto.builder()
                .privateId(goal.privateId)
                .title(goal.title)
                .description(goal.description)
                .endDate(goal.getEndDate())
                .done(goal.getDone())
                .user(goal.getUser()).build();
    }

    public Set<GoalDto> mapToDtos(Set<Goal> goals) {
        return goals.stream()
                .map(this::mapToDto)
                .collect(Collectors.toSet());
    }

    @Data
    @Builder
    public static class GoalDto {
        @JsonIgnore
        private UUID privateId;
        @Max(40)
        @NotNull
        private String title;
        private String description;
        @NotNull
        private Date endDate;
        @NotNull
        private Boolean done = false;
        @NotNull
        private User user;
    }
}
