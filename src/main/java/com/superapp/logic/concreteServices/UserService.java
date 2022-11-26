package com.superapp.logic.concreteServices;

import com.superapp.converters.UserConverter;
import com.superapp.data.UserEntity;
import com.superapp.data.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;

import com.superapp.boundaries.command.user.UserBoundary;
import com.superapp.logic.UsersService;

import java.util.*;


@Service
public class UserService implements UsersService {

    private Map<String, UserEntity> users; // { email: User }
    private UserConverter converter;

    @Autowired
    public UserService(UserConverter converter) {
        this.converter = converter;
    }

    @PostConstruct
    public void setup() {
        this.users = Collections.synchronizedMap(new HashMap<>());
    }

    @Override
    public UserBoundary createUser(UserBoundary user) {
        users.put(user.getUserId().getEmail(), this.converter.toEntity(user));
        //TODO: add newly created user to DB
        return user;
    }

    @Override
    public UserBoundary login(String userSuperApp, String userEmail) {
        if (!userSuperApp.equals("2023a.noam.levy")) // TODO: change to super app name from application.properties
            throw new RuntimeException("Unknown superApp");
        UserEntity user = this.users.get(userEmail);
        if (user == null || !user.getSuperApp().equals(userSuperApp) || !user.getEmail().equals(userEmail))
            throw new RuntimeException("Unknown user");

        return this.converter.toBoundary(user);
    }

    @Override
    public UserBoundary updateUser(String userSuperApp, String userEmail, UserBoundary update) {
        if (!userSuperApp.equals("2023a.noam.levy")) // TODO: change to super app name from application.properties
            throw new RuntimeException("Unknown superApp");
        UserEntity user = this.users.get(userEmail);
        if (user == null || !user.getSuperApp().equals(userSuperApp) || !user.getEmail().equals(userEmail))
            throw new RuntimeException("Unknown user");

        try {
            user.setUsername(update.getUsername());
            user.setAvatar(update.getAvatar());
            user.setRole(UserRole.valueOf(update.getRole()));
        } catch (Exception e) {
            throw new RuntimeException("Cannot update user " + userEmail);
        }
        return this.converter.toBoundary(user);
    }

    @Override
    public List<UserBoundary> getAllUsers() {
        ArrayList<UserBoundary> rv = new ArrayList<>();
        this.users.values().forEach(user -> {
            rv.add(this.converter.toBoundary(user));
        });
        return rv;
    }

    @Override
    public void deleteAllUsers() { this.users.clear(); } // TODO: clear all users from DB
}
