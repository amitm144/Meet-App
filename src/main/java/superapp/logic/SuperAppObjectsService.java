package superapp.logic;

import superapp.boundaries.object.SuperAppObjectBoundary;

import java.util.List;

public interface SuperAppObjectsService {
    public SuperAppObjectBoundary createObject(SuperAppObjectBoundary object);

    public SuperAppObjectBoundary updateObject(String objectSuperapp, String internalObjectId, SuperAppObjectBoundary update);

    public SuperAppObjectBoundary getSpecificObject(String objectSuperApp, String internalObjectId);

    public List<SuperAppObjectBoundary> getAllObjects();

    public void deleteAllObjects();
}
