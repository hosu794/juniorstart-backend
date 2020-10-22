package com.juniorstart.juniorstart.util;

import com.juniorstart.juniorstart.model.Goal;

import java.util.List;
import java.util.stream.Collectors;

/** Class for mapping dto to entity and vice versa 22.10.2020
 * @author Dawid Wit
 * @version 1.0
 * @since 1.0
 */
public class ClassMapper {

    /** Map GoalDto to Goal entity.
     * @param dto of Goal.
     */
    public static Goal mapToEntity(Goal.GoalDto dto) {
        return Goal.builder()
                .id(dto.getId())
                .name(dto.getName())
                .goalType(dto.getGoalType())
                .description(dto.getDescription())
                .isEnded(dto.isEnded())
                .endDate(dto.getEndDate()).build();
    }

    /** Map Goal entity to GoalDto .
     * @param goal entity.
     */
    public static Goal.GoalDto mapToDto(Goal goal) {
        return Goal.GoalDto.builder()
                .id(goal.getId())
                .name(goal.getName())
                .goalType(goal.getGoalType())
                .description(goal.getDescription())
                .isEnded(goal.isEnded())
                .endDate(goal.getEndDate()).build();
    }

    /** Map list of Goals to list of GoalsDto.
     * @param goals list of entities.
     */
    public static List<Goal.GoalDto> mapToDtos(List<Goal> goals) {
        return goals.stream()
                .map(ClassMapper::mapToDto)
                .collect(Collectors.toList());
    }
}
