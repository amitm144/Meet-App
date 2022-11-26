package com.superapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import com.superapp.boundaries.command.user.UserBoundary;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import com.superapp.logic.UsersService;


import com.superapp.logic.UsersService;
import com.superapp.boundaries.command.user.UserBoundary;


@RestController
public class UsersController {
    private UsersService usersService;

    @Autowired
    public void setMessageService(UsersService usersService) {
        this.usersService = usersService;
    }

    @RequestMapping(
            path= {"/superapp/users/login/{superapp}/{email}"},
            method = {RequestMethod.GET},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public Object login (@PathVariable("superapp") String superapp, @PathVariable("email") String email) {
        return this.usersService.login(superapp, email);
    }

    @RequestMapping(
            path= {"/superapp/users"},
            method = {RequestMethod.POST},
            produces = {MediaType.APPLICATION_JSON_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    public Object createUser (@RequestBody UserBoundary user ) {
        return this.usersService.createUser(user);
    }

    @RequestMapping(
            path= {"/superapp/users/{superapp}/{userEmail}"},
            method = {RequestMethod.PUT})
    public void update (
            @PathVariable("superapp") String superapp,
            @PathVariable("userEmail") String email,
            @RequestBody UserBoundary update) {
        this.usersService.updateUser(superapp, email, update);
    }
}