package com.juniorstart.juniorstart.payload;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LoginRequest {
    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;


}
