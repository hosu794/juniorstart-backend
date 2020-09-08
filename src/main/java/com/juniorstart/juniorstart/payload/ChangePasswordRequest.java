package com.juniorstart.juniorstart.payload;

import com.juniorstart.juniorstart.payload.interfaces.InterfaceChangeRequest;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.util.UUID;

/** Class for method changeEmail from UserService
 * @author Dawid Wit
 * @version 1.0
 * @since 1.0
 */
@Getter
public class ChangePasswordRequest implements InterfaceChangeRequest {
    @Email
    private final String newPassword;
    @NotBlank
    private final UUID privateId;
    @NotBlank
    private final String Password;

    /** Creates an object ot change email by UserService.changeEmail.
     * @param newPassword new password for account.
     * @param privateId of user who change email.
     * @param oldPassword old password for account.
     */
    public ChangePasswordRequest(@Min(6) @Max(20) String newPassword, UUID privateId, String oldPassword) {
        this.newPassword = newPassword;
        this.privateId = privateId;
        this.Password = oldPassword;
    }
}