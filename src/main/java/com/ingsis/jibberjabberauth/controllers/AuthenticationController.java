package com.ingsis.jibberjabberauth.controllers;

import com.ingsis.jibberjabberauth.models.AuthenticationRequest;
import com.ingsis.jibberjabberauth.models.RegisterUserDto;
import com.ingsis.jibberjabberauth.services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthenticationController {
    @Autowired
    AuthenticationService authenticationService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws BadCredentialsException {
        return authenticationService.createAuthenticationToken(authenticationRequest);

    }

    @GetMapping(value = "/validate-token")
    public ResponseEntity<?> tokenValidation(@RequestHeader(value = "Authorization") String token){
        return authenticationService.tokenValidation(token);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/register")
    public void register(@RequestBody RegisterUserDto user){
        authenticationService.register(user);
    }
}
