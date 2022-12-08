package com.superapp.data;


import com.superapp.util.wrappers.UserIdWrapper;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;
import java.util.Map;

public class ObjectEntity {

    private String objectId;
    private String superapp;
    private String type;
    private String alias;
    private boolean active;
    private Date creationTimestamp;
    private UserIdWrapper createdBy;
    private Map<String, Object> objectDetails;


    public ObjectEntity() {}

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getSuperapp() {
        return superapp;
    }

    @Value("${spring.application.name}")
    public void setSuperapp(String superapp) {
        this.superapp = superapp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Date getCreationTimestamp() {
        return creationTimestamp;
    }

    public void setCreationTimestamp(Date creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    public UserIdWrapper getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UserIdWrapper createdBy) {
        this.createdBy = createdBy;
    }

    public Map<String, Object> getObjectDetails() {
        return objectDetails;
    }

    public void setObjectDetails(Map<String, Object> objectDetails) {
        this.objectDetails = objectDetails;
    }


    @Override
    public String toString() {
        return "ObjectEntity{" +
                "objectId=" + objectId +
                ", superApp='" + superapp + '\'' +
                ", type='" + type + '\'' +
                ", alias='" + alias + '\'' +
                ", active=" + active +
                ", creationTimestamp=" + creationTimestamp +
                ", createdBy=" + createdBy +
                ", objectDetails=" + objectDetails +
                '}';
    }
}
