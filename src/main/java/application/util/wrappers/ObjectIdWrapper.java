package application.util.wrappers;

import application.boundaries.command.ObjectIdBoundary;
//import com.fasterxml.jackson.annotation.JsonTypeName;

//@JsonTypeName("objectId")
public class ObjectIdWrapper /*implements ObjectWrapper*/ {

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
