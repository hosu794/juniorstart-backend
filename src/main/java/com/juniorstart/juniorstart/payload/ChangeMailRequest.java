package com.juniorstart.juniorstart.payload;

import com.juniorstart.juniorstart.payload.interfaces.InterfaceChangeRequest;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

/** Class for method changeEmail from UserService
 * @author Dawid Wit
 * @version 1.1
 * @since 1.0
 */
@Getter
@NoArgsConstructor
public class ChangeMailRequest implements InterfaceChangeRequest {
    @Email
    private String email;
    @NotBlank
    private UUID userPrivateId;
    @NotBlank
    private String userPassword;

    /** Creates an object ot change email by UserService.changeEmail.
     * @param email new mail for account.
     * @param privateId of user who change email.
     * @param password for account.
     */
    public ChangeMailRequest(@Max(40) String email, UUID privateId, String password) {
        this.email = email;
        this.userPrivateId = privateId;
        this.userPassword = password;
    }

}