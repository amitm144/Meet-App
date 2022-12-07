package com.superapp.util.wrappers;

import com.superapp.boundaries.object.ObjectIdBoundary;

public class ObjectIdWrapper {

    private ObjectIdBoundary objectId;

    public ObjectIdWrapper() {}

    public ObjectIdWrapper(ObjectIdBoundary objectId) { this.objectId = objectId; }

    public ObjectIdBoundary getObjectId() { return objectId; }

    public void setObjectId(ObjectIdBoundary objectId) { this.objectId = objectId; }

    @Override
    public String toString() {
        return "ObjectIdWrapper{" +
                "objectId=" + objectId +
                '}';
    }
}
