package com.superapp.boundaries.command;

import org.springframework.beans.factory.annotation.Value;

import java.util.Random;

public class MiniAppCommandIdBoundary {

    private String superapp ;
    private String miniapp ;
    private String internalCommandId;

    public MiniAppCommandIdBoundary() { }

    public MiniAppCommandIdBoundary(String miniApp, String internalCommandId) {
        if (miniApp.isBlank() || internalCommandId.isBlank())
            throw new RuntimeException("command id or miniApp name cannot be blank");
        this.miniapp = miniApp;
        this.superapp = "2023a.noam.levy";
        this.internalCommandId = internalCommandId;
    }

    public MiniAppCommandIdBoundary(String miniApp) {
        this();
        this.miniapp = miniapp;
    }

    public String getSuperapp() {
        return superapp;
    }

    @Value("${spring.application.name}")
    public void setSuperapp(String superapp) {
        this.superapp = superapp;
    }

    public String getMiniapp() {
        return miniapp;
    }

    public void setMiniapp(String miniApp) {
        this.miniapp = miniapp;
    }

    public String getInternalCommandId() {
        return internalCommandId;
    }

    public void setInternalCommandId(String internalCommandId) {
        this.internalCommandId = internalCommandId;
    }

    @Override
    public String toString() {
        return "CommandIdBoundary{" +
                "superapp='" + superapp + '\'' +
                ", miniapp='" + miniapp + '\'' +
                ", internalCommandId='" + internalCommandId + '\'' +
                '}';
    }
}
