package com.superapp.boundaries.object;

import java.util.Random;

public class ObjectIdBoundary {

    private String superApp ;
    private String internalObjectId;

    public ObjectIdBoundary() {
        this.superApp = "2023a.noam.levy";
        int id = new Random().nextInt(1000);
        this.internalObjectId = Integer.toString(id);
    }

    public ObjectIdBoundary(String internalObjectId) {
        this();
        this.internalObjectId = internalObjectId;
    }

    public String getSuperApp() {
        return superApp;
    }

    public void setSuperApp(String superApp) {
        this.superApp = superApp;
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
                "superApp='" + superApp + '\'' +
                ", internalObjectId='" + internalObjectId + '\'' +
                '}';
    }
}
