package superapp.util.wrappers;

import superapp.boundaries.object.SuperAppObjectIdBoundary;

public class SuperAppObjectIdWrapper {

    private SuperAppObjectIdBoundary objectId;

    public SuperAppObjectIdWrapper() {}

    public SuperAppObjectIdWrapper(SuperAppObjectIdBoundary objectId) { this.objectId = objectId; }

    public SuperAppObjectIdBoundary getObjectId() { return objectId; }

    public void setObjectId(SuperAppObjectIdBoundary objectId) { this.objectId = objectId; }

    @Override
    public String toString() {
        return "ObjectIdWrapper{" +
                "objectId=" + objectId +
                '}';
    }
}
