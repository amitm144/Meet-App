package com.superapp.boundaries.user;

import com.superapp.util.EmailChecker;
import org.springframework.beans.factory.annotation.Value;

public class UserIdBoundary {

    private String superapp ;
    private String email ;

    public UserIdBoundary() {}

    public UserIdBoundary(String email) {
        this();
        if (!EmailChecker.isValidEmail(email))
            throw new RuntimeException("invalid email");

        this.email = email;
    }

    public UserIdBoundary(String superapp, String email) {
        if (!EmailChecker.isValidEmail(email))
            throw new RuntimeException("invalid email");
        if (superapp.isBlank())
            throw  new RuntimeException("super-app name cannot be empty");

        this.superapp = superapp;
        this.email = email;
    }

    public String getSuperapp() {
        return superapp;
    }

    @Value("${spring.application.name}")
    public void setSuperapp(String superapp) {
        this.superapp = superapp;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        if (EmailChecker.isValidEmail(email))
            this.email = email;
        else
            throw new RuntimeException("Invalid email");
    }

    @Override
    public String toString() {
        return "UserIdBoundary{" +
                "superapp='" + superapp + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
