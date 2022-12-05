package com.superapp.logic.concreteServices;

import com.superapp.boundaries.object.ObjectBoundary;
import com.superapp.converters.ObjectConverter;
import com.superapp.data.ObjectEntity;
import com.superapp.logic.ObjectsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

@Service
public class ObjectService implements ObjectsService {

    private Map<String, ObjectEntity> objects; // { object id : object }
    private ObjectConverter converter;

    @Autowired
    public ObjectService(ObjectConverter converter) {
        this.converter = converter;
    }

    @PostConstruct
    public void setup() {this.objects = Collections.synchronizedMap(new HashMap<>()); }

    @Override
    public ObjectBoundary createObject(ObjectBoundary object) {
        if ( objects.containsKey(object.getObjectId().getInternalObjectId()) ) {
            throw new RuntimeException("Object already exists");
        }
        objects.put(object.getObjectId().getInternalObjectId() , converter.toEntity(object));
        return object;
    }

    @Override
    public ObjectBoundary updateObject(String objectSuperApp, String internalObjectId, ObjectBoundary update) {
        if (!objects.containsKey(internalObjectId)) {
            throw new RuntimeException("Object does not (or doesn't) exist");
        }
        if ( internalObjectId != update.getObjectId().getInternalObjectId() ) {
            throw new RuntimeException("Cannot change object ID");
        }
        objects.replace(internalObjectId , converter.toEntity(update)) ;
        return update;
    }

    @Override
    public ObjectBoundary getSpecificObject(String objectSuperApp, String internalObjectId) {
        if ( !objects.containsKey(internalObjectId) )
            throw new RuntimeException("Object does not (or doesn't) exist");
        return this.converter.toBoundary(objects.get(internalObjectId));
    }

    @Override
    public List<ObjectBoundary> getAllObjects() {
        ArrayList<ObjectBoundary> AllObjects = new ArrayList<>();
        for (ObjectEntity obj: objects.values()) {
            AllObjects.add(converter.toBoundary(obj));
        }
        return AllObjects;
    }

    @Override
    public void deleteAllObjects() {
        objects.clear();
    }
}
