package superapp.data;

import superapp.boundaries.user.UserIdBoundary;
import superapp.util.wrappers.UserIdWrapper;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name="Objects")
@IdClass(SuperappObjectPK.class)
public class SuperAppObjectEntity {
    @Id
    private String objectId;
    @Id
    private String superapp;
    private String type;
    private String alias;
    private boolean active;
    private Date creationTimestamp;
    private String userEmail;
    private String userSuperapp;
    @Lob
    private String objectDetails;
    @ManyToMany
    @JoinTable(name="objects_relations")
    private Set<SuperAppObjectEntity> parents;
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "parents")
    private Set<SuperAppObjectEntity> children;

    public SuperAppObjectEntity() {}

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
                new UserIdBoundary(this.userSuperapp, this.userEmail));
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

    public Set<SuperAppObjectEntity> getParents() {
        return parents;
    }

    public void setParents(Set<SuperAppObjectEntity> parent) {
        this.parents = parent;
    }

    public boolean addParent(SuperAppObjectEntity parent) {
        return parent != this && !this.children.contains(parent) && this.parents.add(parent);
    }

    public Set<SuperAppObjectEntity> getChildren() {
        return children;
    }

    public void setChildren(Set<SuperAppObjectEntity> childObjects) {
        this.children = childObjects;
    }

    public boolean addChild(SuperAppObjectEntity child) {
        return child != this && !this.parents.contains(child) && this.children.add(child);
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
