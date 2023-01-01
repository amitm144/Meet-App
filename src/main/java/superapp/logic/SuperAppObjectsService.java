package superapp.logic;

import superapp.boundaries.object.SuperAppObjectBoundary;
import superapp.boundaries.object.SuperAppObjectIdBoundary;

import java.util.Collection;
import java.util.List;

public interface SuperAppObjectsService {
    public SuperAppObjectBoundary createObject(SuperAppObjectBoundary object);
    @Deprecated
    public SuperAppObjectBoundary updateObject(String objectSuperapp, String internalObjectId, SuperAppObjectBoundary update);
    @Deprecated
    public void bindNewChild(String parentSuperapp, String parentObjectId, SuperAppObjectIdBoundary newChild);
    @Deprecated
    public SuperAppObjectBoundary getSpecificObject(String objectSuperApp, String internalObjectId);

    @Deprecated
    public List<SuperAppObjectBoundary> getAllObjects();

    public void deleteAllObjects();

}
