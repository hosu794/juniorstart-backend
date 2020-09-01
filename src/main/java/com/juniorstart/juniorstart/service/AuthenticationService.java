package com.juniorstart.juniorstart.service;

import com.juniorstart.juniorstart.exception.BadRequestException;
import com.juniorstart.juniorstart.model.AuthProvider;
import com.juniorstart.juniorstart.model.User;
import com.juniorstart.juniorstart.payload.ApiResponse;
import com.juniorstart.juniorstart.payload.AuthResponse;
import com.juniorstart.juniorstart.payload.LoginRequest;
import com.juniorstart.juniorstart.payload.SignUpRequest;
import com.juniorstart.juniorstart.repository.UserRepository;
import com.juniorstart.juniorstart.security.TokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;


/** Represents an authentication service.
 * @author Grzegorz Szczęsny
 * @version 1.0
 * @since 1.0
 */
@Service
@Slf4j
public class AuthenticationService {

    public AuthenticationService(AuthenticationManager authenticationManager, UserRepository userRepository, PasswordEncoder passwordEncoder, TokenProvider tokenProvider) {
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
        this.userRepository = userRepository;

    }

    final private AuthenticationManager authenticationManager;
    final private UserRepository userRepository;
    final private PasswordEncoder passwordEncoder;
    final private TokenProvider tokenProvider;


    /** Authenticate a given user by credentials.
     * @param loginRequest The user’s credentials to login.
     * @return a bearer token
     * @throws AuthenticationException if credentials are not correct
     */
    public ResponseEntity<?> authenticationUser(LoginRequest loginRequest) {


        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = tokenProvider.createToken(authentication);
        return ResponseEntity.ok(new AuthResponse(token));

    }


    /** Create a new user from a given credentials.
     * @param signUpRequest The user’s credentials to register.
     * @return a {@link ResponseEntity} with a message whether it registered
     * @throws BadRequestException if email is already in use
     */
    public ResponseEntity<?> registerUser(SignUpRequest signUpRequest) {
        if(userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new BadRequestException("Email address already in use.");
        }


        User user = new User();
        user.setName(signUpRequest.getName());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(signUpRequest.getPassword());
        user.setProvider(AuthProvider.local);

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User result = userRepository.save(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/user/me")
                .buildAndExpand(result.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "User registered successfully@"));
    }



}
