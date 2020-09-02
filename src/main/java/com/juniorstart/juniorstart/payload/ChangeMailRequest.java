package com.juniorstart.juniorstart.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

/** Class for method changeEmail from UserService
 * @author Dawid Wit
 * @version 1.0
 * @since 1.0
 */
@Getter
public class ChangeMailRequest {
    @Email
    String email;
    @NotBlank
    String name;
    @NotBlank
    String password;

    /** Creates an object ot change email by UserService.changeEmail.
     * @param email new mail for account.
     * @param name name user who change email.
     * @param password for account.
     */
    public ChangeMailRequest(@Email @Max(40) String email, @NotBlank String name, @NotBlank String password) {
        this.email = email;
        this.name = name;
        this.password = password;
    }
}
