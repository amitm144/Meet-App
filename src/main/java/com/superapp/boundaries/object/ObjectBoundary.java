package com.superapp.boundaries.object;

import com.superapp.boundaries.command.user.UserIdBoundary;
import com.superapp.util.wrappers.UserIdWrapper;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ObjectBoundary {
    private ObjectIdBoundary objectId;
    private String type;
    private String alias;
    private boolean active;
    private Date creationTimeStamp;
    private UserIdWrapper createdBy;
    private Map<String, Object> objectDetails;

    public ObjectBoundary() {
    }

    public ObjectBoundary(ObjectIdBoundary objectId, String type, String alias,
                          Map<String, Object> objectDetails, UserIdWrapper createdBy){
        this.objectId = objectId;
        this.type = type;
        this.alias = alias;
        this.active = true;
        this.creationTimeStamp = new Date();
        this.createdBy = createdBy;
        this.objectDetails = objectDetails;
    }

    public ObjectIdBoundary getObjectId() {
        return objectId;
    }

    public void setObjectId(ObjectIdBoundary objectId) {
        this.objectId = objectId;
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

    public Date getCreationTimeStamp() {
        return creationTimeStamp;
    }

    public void setCreationTimeStamp(Date creationTimeStamp) {
        this.creationTimeStamp = creationTimeStamp;
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

    public static ArrayList<ObjectBoundary> getNRandomObjects(int n) {
        ArrayList<ObjectBoundary> userBoundaries = new ArrayList<ObjectBoundary>();
        Map<String, Object> map= new HashMap<String, Object>();
        map.put("key", "value for example");
        for (int i = 0; i < n; i++) {
            userBoundaries.add(new ObjectBoundary(
                    new ObjectIdBoundary(String.format("id%d", i)),
                    "example-type",
                    "example-alias",
                    map,
                    new UserIdWrapper(new UserIdBoundary(String.format("temp%d@gmail.com", i)))));
        }
        return userBoundaries;
    }

    @Override
    public String toString() {
        return "ObjectBoundary{" +
                "objectId='" + objectId + '\'' +
                ", type='" + type + '\'' +
                ", alias='" + alias + '\'' +
                ", active=" + active +
                ", creationTimeStamp=" + creationTimeStamp +
                ", createBy=" + createdBy +
                ", objectDetails=" + objectDetails +
                '}';
    }
}
