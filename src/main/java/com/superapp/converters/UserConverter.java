package com.superapp.converters;

//import com.fasterxml.jackson.databind.ObjectMapper;
import com.superapp.boundaries.command.user.UserBoundary;
import com.superapp.data.UserEntity;
import com.superapp.data.UserRole;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {

//    private ObjectMapper mapper;

    public UserConverter() {
//        this.mapper = new ObjectMapper();
    }

    public UserEntity toEntity(UserBoundary user) {
        UserEntity rv = new UserEntity();
        rv.setSuperApp(user.getUserId().getSuperApp());
        rv.setEmail(user.getUserId().getEmail());
        rv.setUsername(user.getUsername());
        rv.setRole(UserRole.valueOf(user.getRole()));
        rv.setAvatar(user.getAvatar());

        return rv;
    }

    public UserBoundary toBoundary(UserEntity user) {
        UserBoundary rv = new UserBoundary();
        rv.setEmail(user.getEmail());
        rv.setRole(user.getRole().name());
        rv.setUsername(user.getUsername());
        rv.setAvatar(user.getAvatar());

        return rv;
    }
}
