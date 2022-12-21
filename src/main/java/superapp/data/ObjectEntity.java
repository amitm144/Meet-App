package superapp.data;

import superapp.boundaries.user.UserIdBoundary;
import superapp.util.wrappers.UserIdWrapper;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name="Objects")
public class ObjectEntity {
    @Id
    private String objectId;
    private String superapp;
    private String type;
    private String alias;
    private boolean active;
    private Date creationTimestamp;
    private String userEmail;
    private String userSuperapp;
    private String objectDetails;

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

    @Temporal(TemporalType.TIMESTAMP)
    public Date getCreationTimestamp() {
        return creationTimestamp;
    }

    public void setCreationTimestamp(Date creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserSuperapp() {
        return userSuperapp;
    }

    public void setUserSuperapp(String userSuperapp) {
        this.userSuperapp = userSuperapp;
    }

    public UserIdWrapper getCreatedBy() {
        return new UserIdWrapper(
                new UserIdBoundary(this.userSuperapp,this.userEmail));
    }

    public void setCreatedBy(UserIdBoundary createdBy) {
        this.userEmail = createdBy.getEmail();
        this.userSuperapp = createdBy.getSuperapp();
    }

    @Lob
    public String getObjectDetails() {
        return objectDetails;
    }

    public void setObjectDetails(String objectDetails) {
        this.objectDetails = objectDetails;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ObjectEntity object = (ObjectEntity) o;
        return objectId.equals(object.objectId) && superapp.equals(object.superapp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(objectId, superapp);
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
                ", createdBy=" + new UserIdBoundary(this.userSuperapp, this.userEmail).toString() +
                ", objectDetails=" + objectDetails +
                '}';
    }
}
