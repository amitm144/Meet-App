package application.logic;

import application.boundaries.object.ObjectBoundary;

public interface ObjectsService {
    ObjectBoundary createObject(ObjectBoundary object);

    ObjectBoundary updateObject(String objectSuperApp, String internalObjectId, ObjectBoundary update);

    ObjectBoundary getSpecificObject(String objectSuperApp, String internalObjectId);

    ObjectBoundary[] getAllObjects();

    void deleteAllObjects();
}
