package com.superapp.logic;

import com.superapp.boundaries.object.ObjectBoundary;

import java.util.List;

public interface ObjectsService {
    public ObjectBoundary createObject(ObjectBoundary object);

    public ObjectBoundary updateObject(String objectSuperapp, String internalObjectId, ObjectBoundary update);

    public ObjectBoundary getSpecificObject(String objectSuperApp, String internalObjectId);

    public List<ObjectBoundary> getAllObjects();

    public void deleteAllObjects();
}
