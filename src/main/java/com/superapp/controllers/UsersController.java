package com.superapp.controllers;

import com.superapp.boundaries.user.UserBoundary;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;


@RestController
public class UsersController {

    @RequestMapping(
            path= {"/superapp/users/login/{superapp}/{email}"},
            method = {RequestMethod.GET},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public UserBoundary login (@PathVariable("superapp") String superapp, @PathVariable("email") String email) {
        String username = email.split("@")[0];
        return new UserBoundary(superapp, email, "user", username, "A" );
    }

    @RequestMapping(
            path= {"/superapp/users"},
            method = {RequestMethod.POST},
            produces = {MediaType.APPLICATION_JSON_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    public UserBoundary createUser (@RequestBody UserBoundary user ) { return user; }

    @RequestMapping(
            path= {"/superapp/users/{superapp}/{userEmail}"},
            method = {RequestMethod.PUT},
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    public void update (@PathVariable("superapp") String superapp , @PathVariable("userEmail") String email) { }

}