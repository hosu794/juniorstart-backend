package com.juniorstart.juniorstart.payload;

import com.juniorstart.juniorstart.payload.interfaces.InterfaceChangeRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

/** Class for get user goal/s 26.09.2020
 * @author Dawid Wit
 * @version 1.0
 * @since 1.0
 */
@Getter
@NoArgsConstructor
public class GetGoalRequest implements InterfaceChangeRequest {
    @NotBlank
    private UUID userPrivateId;
    @NotBlank
    private String userPassword;

    /** Creates an object ot change status by UserService.changeStatus.
     * @param userId of user.
     * @param userPassword of user.
     */
    public GetGoalRequest(@NotBlank UUID userId, @NotBlank String userPassword) {
        this.userPrivateId = userId;
        this.userPassword = userPassword;
    }
}
