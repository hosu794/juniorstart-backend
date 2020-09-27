package com.juniorstart.juniorstart.payload;

import com.juniorstart.juniorstart.payload.interfaces.InterfaceChangeRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

/** Class for delete user goal 26.09.2020
 * @author Noboseki
 * @version 1.0
 * @since 1.0
 */
@Getter
@NoArgsConstructor
public class DeleteGoalRequest implements InterfaceChangeRequest {
    @NotBlank
    private UUID goalId;
    @NotBlank
    private UUID userPrivateId;
    @NotBlank
    private String userPassword;

    /** Creates an object to delete goal.
     * @param goalId of delete goal.
     * @param userId of deleting goal.
     * @param userPassword of deleting goal.
     */
    public DeleteGoalRequest(UUID goalId, UUID userId, String userPassword) {
        this.goalId = goalId;
        this.userPrivateId = userId;
        this.userPassword = userPassword;
    }
}
