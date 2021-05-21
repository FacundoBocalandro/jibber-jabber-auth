package com.ingsis.jibberjabberauth.services;

import com.ingsis.jibberjabberauth.models.AuthenticationRequest;
import com.ingsis.jibberjabberauth.models.AuthenticationResponse;
import com.ingsis.jibberjabberauth.models.RoleResponse;
import com.ingsis.jibberjabberauth.security.JwtUtil;
import com.ingsis.jibberjabberauth.security.MyUserDetailsService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;

    private final MyUserDetailsService myUserDetailsService;

    private final JwtUtil jwtUtil;

    public AuthenticationService(AuthenticationManager authenticationManager, MyUserDetailsService myUserDetailsService, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.myUserDetailsService = myUserDetailsService;
        this.jwtUtil = jwtUtil;
    }

    public ResponseEntity<?> createAuthenticationToken(AuthenticationRequest authenticationRequest) throws BadCredentialsException {
        //Could throw BadCredentialsException
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
        );

        final UserDetails userDetails = myUserDetailsService.loadUserByUsername(authenticationRequest.getUsername());

        final String jwt = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }

    public ResponseEntity<?> tokenValidation(String token) {
        String role = jwtUtil.extractRole(token.substring(6));
        return ResponseEntity.ok(new RoleResponse(role));
    }
}
