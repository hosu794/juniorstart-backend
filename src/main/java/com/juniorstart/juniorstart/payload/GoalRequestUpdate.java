package com.juniorstart.juniorstart.payload;

import com.juniorstart.juniorstart.model.Goal;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

/** Class for method update from GoalService 22.10.2020
 * @author Dawid Wit
 * @version 1.0
 * @since 1.0
 */
@Getter
@NoArgsConstructor
public class GoalRequestUpdate {
    @NotBlank
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private Goal.GoalDto dto;

    @NotBlank
    private String name;

    /** Creates an object ot update goal by GoalService.update.
     * @param email of user who change status.
     * @param password of user account.
     * @param dto new DtoGoal for user.
     * @param name old of changed goal.
     */
    public GoalRequestUpdate(String email, String password, Goal.GoalDto dto, String name) {
        this.email = email;
        this.password = password;
        this.dto = dto;
        this.name = name;
    }
}
