package com.juniorstart.juniorstart.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.util.UUID;

/** Class for method changeEmail from UserService
 * @author Dawid Wit
 * @version 1.1
 * @since 1.0
 */
@Getter
public class ChangeMailRequest {
    @Email
    String email;
    @NotBlank
    UUID privateId;
    @NotBlank
    String password;

    /** Creates an object ot change email by UserService.changeEmail.
     * @param email new mail for account.
     * @param privateId of user who change email.
     * @param password for account.
     */
    public ChangeMailRequest(@Email @Max(40) String email, UUID privateId, @NotBlank String password) {
        this.email = email;
        this.privateId = privateId;
        this.password = password;
    }
}