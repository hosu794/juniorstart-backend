package com.juniorstart.juniorstart.payload;

import com.juniorstart.juniorstart.model.audit.UserStatus;
import com.juniorstart.juniorstart.payload.interfaces.InterfaceChangeRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

/** Class for method changeStatus from UserService 16.09.2020
 * @author Dawid Wit
 * @version 1.1
 * @since 1.0
 */
@Getter
@ToString
public class ChangeStatusRequest implements InterfaceChangeRequest {
    @NotBlank
    private final UUID privateId;
    @NotBlank
    private final String password;
    @NotBlank
    private final UserStatus userStatus;

    /** Creates an object ot change status by UserService.changeStatus.
     * @param privateId of user who change status.
     * @param password for account.
     * @param userStatus new status for user.
     */
    public ChangeStatusRequest(UserStatus userStatus,
                               UUID privateId,
                               String password) {
        this.privateId = privateId;
        this.password = password;
        this.userStatus = userStatus;
    }
}
