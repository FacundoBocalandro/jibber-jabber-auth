package com.ingsis.jibberjabberauth.services;

import com.ingsis.jibberjabberauth.dto.UserInfoDto;
import com.ingsis.jibberjabberauth.models.*;
import com.ingsis.jibberjabberauth.repository.UserRepository;
import com.ingsis.jibberjabberauth.security.JwtUtil;
import com.ingsis.jibberjabberauth.security.MyUserDetailsService;
import javassist.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;

    private final MyUserDetailsService myUserDetailsService;

    private final UserRepository userRepository;

    private final JwtUtil jwtUtil;

    public AuthenticationService(AuthenticationManager authenticationManager, MyUserDetailsService myUserDetailsService, UserRepository userRepository, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.myUserDetailsService = myUserDetailsService;
        this.userRepository = userRepository;
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
//        cookie.setSecure(true);
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

    public void register(RegisterUserDto userDto) {
        User user = new User(userDto.getFirstName(), userDto.getLastName(), userDto.getEmail(), userDto.getUsername(), userDto.getPassword(), "ROLE_USER");
        userRepository.save(user);
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

    public UserInfoDto getUserInfo(String token) {
        return UserInfoDto.fromUser(jwtUtil.getUser(token));
    }

    public List<UserInfoDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(UserInfoDto::fromUser).collect(Collectors.toList());
    }

    public void followUser(String token, long id) throws NotFoundException {
        User user = jwtUtil.getUser(token);
        User userToFollow = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
        if (!user.getFollowing().contains(userToFollow)) {
            user.addFollowing(userToFollow);
            userRepository.save(user);
        }
    }

    public void unfollowUser(String token, long id) throws NotFoundException {
        User user = jwtUtil.getUser(token);
        User userToUnfollow = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
        if (user.getFollowing().contains(userToUnfollow)) {
            user.deleteFollowing(userToUnfollow);
            userRepository.save(user);
        }
    }
}
