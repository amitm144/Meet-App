package com.superapp.logic;

import com.superapp.boundaries.object.ObjectBoundary;

public interface ObjectsService {
    ObjectBoundary createObject(ObjectBoundary object);

    ObjectBoundary updateObject(String objectSuperApp, String InternalObjectId, ObjectBoundary update);

    ObjectBoundary getSpecificObject(String objectSuperApp, String internalObjectId);

    ObjectBoundary[] getAllObjects();

    void deleteAllObjects();
}
