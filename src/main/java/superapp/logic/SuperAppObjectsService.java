package superapp.logic;

import superapp.boundaries.object.SuperAppObjectBoundary;
import superapp.boundaries.object.SuperAppObjectIdBoundary;

import java.util.List;

public interface SuperAppObjectsService {
    public SuperAppObjectBoundary createObject(SuperAppObjectBoundary object);

    public SuperAppObjectBoundary updateObject(String objectSuperapp, String internalObjectId, SuperAppObjectBoundary update);

    public void bindNewChild(String parentSuperapp, String parentObjectId, SuperAppObjectIdBoundary newChild);

    public SuperAppObjectBoundary getSpecificObject(String objectSuperApp, String internalObjectId);

    public List<SuperAppObjectBoundary> getChildren(String objectSuperapp, String internalObjectId);

    public List<SuperAppObjectBoundary> getParents(String objectSuperapp, String internalObjectId);

    public List<SuperAppObjectBoundary> getAllObjects();

    public void deleteAllObjects();
}
