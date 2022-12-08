package com.superapp.boundaries.command;

import org.springframework.beans.factory.annotation.Value;

import java.util.Random;

public class CommandIdBoundary {

    private String superapp ;
    private String miniapp ;
    private String internalCommandId;

    public CommandIdBoundary() {
        this.superapp = "2023a.noam.levy";
        int id = new Random().nextInt(1000);
        this.internalCommandId = Integer.toString(id);
    }

    public CommandIdBoundary(String miniapp, String internalCommandId) {
        if (miniapp.isBlank() || internalCommandId.isBlank())
            throw new RuntimeException("command id or miniapp name cannot be blank");
        this.miniapp = miniapp;
        this.internalCommandId = internalCommandId;
    }

    public CommandIdBoundary(String miniapp) {
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
