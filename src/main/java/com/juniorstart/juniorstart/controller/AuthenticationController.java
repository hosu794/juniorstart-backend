package com.juniorstart.juniorstart.controller;

import com.juniorstart.juniorstart.payload.LoginRequest;
import com.juniorstart.juniorstart.payload.SignUpRequest;
import com.juniorstart.juniorstart.service.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

  public AuthenticationController(AuthenticationService authenticationService) {
    this.authenticationService = authenticationService;
  }

  private final AuthenticationService authenticationService;


    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        return authenticationService.authenticationUser(loginRequest);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        return authenticationService.registerUser(signUpRequest);
    }

}
