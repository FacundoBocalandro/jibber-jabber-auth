package com.ingsis.jibberjabberauth.services;

import com.ingsis.jibberjabberauth.models.*;
import com.ingsis.jibberjabberauth.security.JwtUtil;
import com.ingsis.jibberjabberauth.security.MyUserDetailsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

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

    public ResponseEntity<?> createAuthenticationToken(AuthenticationRequest authenticationRequest, HttpServletResponse response) throws BadCredentialsException {
        //Could throw BadCredentialsException
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
        );

        final UserDetails userDetails = myUserDetailsService.loadUserByUsername(authenticationRequest.getUsername());

        final String jwt = jwtUtil.generateToken(userDetails);

        // create a cookie
        Cookie cookie = new Cookie("token", jwt);

        // expires in 7 days
        cookie.setMaxAge(7 * 24 * 60 * 60);

        // optional properties
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setPath("/");

        // add cookie to response
        response.addCookie(cookie);

        // return response entity
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<?> tokenValidation(String token) {
        String role = jwtUtil.extractRole(token);
        return ResponseEntity.ok(new RoleResponse(role));
    }

    public ResponseEntity<?> invalidateAuthenticationCookie(HttpServletResponse response) {
        // create a cookie
        Cookie cookie = new Cookie("token", null);
        cookie.setMaxAge(0);
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setPath("/");

        //add cookie to response
        response.addCookie(cookie);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
