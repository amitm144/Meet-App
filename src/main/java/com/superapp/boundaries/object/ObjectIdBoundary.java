package com.superapp.boundaries.object;

import java.util.Random;

public class ObjectIdBoundary {

    private String superapp ;
    private String internalObjectId;

    public ObjectIdBoundary() {
        this.superapp = "2023a.noam.levy";
        int id = new Random().nextInt(1000);
        this.internalObjectId = Integer.toString(id);
    }

    public ObjectIdBoundary(String internalObjectId) {
        this();
        this.internalObjectId = internalObjectId;
    }

    public String getSuperapp() {
        return superapp;
    }

    public void setSuperapp(String superapp) {
        this.superapp = superapp;
    }

    public String getInternalObjectId() {
        return internalObjectId;
    }

    public void setInternalObjectId(String internalObjectId) {
        this.internalObjectId = internalObjectId;
    }

    @Override
    public String toString() {
        return "ObjectIdBoundary{" +
                "superapp='" + superapp + '\'' +
                ", internalObjectId='" + internalObjectId + '\'' +
                '}';
    }
}
