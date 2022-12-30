package superapp.data;

import java.io.Serializable;
import java.util.Objects;

public class SuperappObjectPK implements Serializable {
    /* This class creates composite primary key for SuperAppObject */
    private String superapp;
    private String objectId;

    public SuperappObjectPK() {}

    public SuperappObjectPK(String superapp, String objectId) {
        this.superapp = superapp;
        this.objectId = objectId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SuperappObjectPK object = (SuperappObjectPK)o;
        return objectId.equals(object.objectId) && superapp.equals(object.superapp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(objectId, superapp);
    }

    public String getSuperapp() { return superapp; }

    public void setSuperapp(String superapp) { this.superapp = superapp; }

    public String getObjectId() { return objectId; }

    public void setObjectId(String objectId) { this.objectId = objectId; }
}