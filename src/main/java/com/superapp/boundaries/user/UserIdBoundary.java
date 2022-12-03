package com.superapp.boundaries.user;

import com.superapp.util.EmailChecker;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;

public class UserIdBoundary {

    private String superApp ;
    private String email ;

    public UserIdBoundary() {}

    public UserIdBoundary(String email) {
        this();
        if (!EmailChecker.isValidEmail(email))
            throw new RuntimeException("invalid email");
        this.email = email;
    }

    public UserIdBoundary(String superApp, String email) {
        if (!EmailChecker.isValidEmail(email))
            throw new RuntimeException("invalid email");
        if (superApp.isBlank())
            throw  new RuntimeException("super-app name cannot be empty");
        this.superApp = superApp;
        this.email = email;
    }

    public String getSuperApp() {
        return superApp;
    }

    @PostConstruct
    @Value("${spring.application.name}")
    public void setSuperApp(String superApp) {
        this.superApp = superApp;
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
                "superApp='" + superApp + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
