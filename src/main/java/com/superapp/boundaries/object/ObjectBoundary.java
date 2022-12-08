package com.superapp.boundaries.object;


import com.superapp.util.wrappers.UserIdWrapper;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ObjectBoundary {
    private ObjectIdBoundary objectId;
    private String type;
    private String alias;
    private Boolean active;
    private Date creationTimestamp;
    private UserIdWrapper createdBy;
    private Map<String, Object> objectDetails;

    public ObjectBoundary() {}

    public ObjectBoundary(ObjectIdBoundary objectId, String type, String alias,
                          Map<String, Object> objectDetails, UserIdWrapper createdBy){
        this.objectId = objectId;
        this.type = type;
        this.alias = alias;
        this.active = true;
        this.creationTimestamp = new Date();
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

    public Boolean getActive() { return active; }

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
        return "ObjectBoundary{" +
                "objectId='" + objectId + '\'' +
                ", type='" + type + '\'' +
                ", alias='" + alias + '\'' +
                ", active=" + active +
                ", creationTimeStamp=" + creationTimestamp +
                ", createBy=" + createdBy +
                ", objectDetails=" + objectDetails +
                '}';
    }
}
