package com.superapp.util;

import org.springframework.beans.factory.annotation.Value;

public class SuperappChecker {

    @Value("${spring.application.name}")
    private final String superapp = "2023a.noam.levy"; // TODO: inject value from application.name

    public boolean isValidSuperapp(String superapp) { return this.superapp.equals(superapp); }
}
