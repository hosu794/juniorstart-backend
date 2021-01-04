package com.juniorstart.juniorstart.payload;

import com.juniorstart.juniorstart.payload.interfaces.InterfaceChangeRequest;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.UUID;

/** Class for method changeEmail from UserService
 * @author Dawid Wit
 * @version 1.0
 * @since 1.0
 */
@Builder
@AllArgsConstructor
@Data
@NoArgsConstructor
public class ChangePasswordRequest implements InterfaceChangeRequest {

    private String newPassword;
    @NotBlank
    private UUID privateId;
    @NotBlank
    private String Password;



}