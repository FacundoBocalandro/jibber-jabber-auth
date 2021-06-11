package com.ingsis.jibberjabberauth.controllers;

import com.ingsis.jibberjabberauth.models.RegisterUserDto;
import com.ingsis.jibberjabberauth.services.UserService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @RequestMapping(method = RequestMethod.POST, value = "/register")
    public void register(@RequestBody RegisterUserDto user){
        userService.register(user);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/update-user/{id}")
    public void updateUser(@RequestBody RegisterUserDto user, @PathVariable long id) throws NotFoundException {
        userService.updateUser(user, id);
    }
}
