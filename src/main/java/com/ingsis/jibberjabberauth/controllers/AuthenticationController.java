package com.ingsis.jibberjabberauth.controllers;

import com.ingsis.jibberjabberauth.dto.UserInfoDto;
import com.ingsis.jibberjabberauth.models.AuthenticationRequest;
import com.ingsis.jibberjabberauth.models.RegisterUserDto;
import com.ingsis.jibberjabberauth.services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
public class AuthenticationController {
    @Autowired
    AuthenticationService authenticationService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest, HttpServletResponse response) throws BadCredentialsException {
        return authenticationService.createAuthenticationToken(authenticationRequest, response);
    }

    @GetMapping(value = "/delete-token")
    public ResponseEntity<?> invalidateAuthenticationCookie(HttpServletResponse response) {
        return authenticationService.invalidateAuthenticationCookie(response);
    }

    @GetMapping(value = "/validate-token")
    public ResponseEntity<?> tokenValidation(@CookieValue(value = "token") String token){
        return authenticationService.tokenValidation(token);
    }

    @GetMapping(value = "/user-info")
    public UserInfoDto getUserInfo(@CookieValue(value = "token") String token) {
        return authenticationService.getUserInfo(token);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/register")
    public void register(@RequestBody RegisterUserDto user){
        authenticationService.register(user);
    }
}
