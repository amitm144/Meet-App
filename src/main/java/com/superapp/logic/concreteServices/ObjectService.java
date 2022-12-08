package com.superapp.logic.concreteServices;

import com.superapp.boundaries.object.ObjectBoundary;
import com.superapp.boundaries.object.ObjectIdBoundary;
import com.superapp.converters.ObjectConverter;
import com.superapp.boundaries.data.ObjectEntity;
import com.superapp.logic.ObjectsService;
import com.superapp.util.wrappers.UserIdWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

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
        if (objects.containsKey(object.getObjectId().getInternalObjectId()))
            throw new RuntimeException("Object already exists");

        String ObjectId = object.getObjectId().getInternalObjectId();
        if (ObjectId == null || ObjectId.equals("null"))
            object.getObjectId().setInternalObjectId(String.valueOf(this.objects.size() + 1));

        String superapp = object.getObjectId().getSuperapp();
        if (superapp == null || superapp.equals("null"))
            throw new RuntimeException("Superapp name cannot be empty");

        object.setCreationTimestamp(new Date());
        objects.put(object.getObjectId().getInternalObjectId(), converter.toEntity(object));
        return object;
    }

    @Override
    public ObjectBoundary updateObject(String objectSuperapp,
                                       String internalObjectId,
                                       ObjectBoundary update) {
        ObjectEntity object = this.objects.get(internalObjectId);
        if (object == null || !object.getSuperapp().equals(objectSuperapp))
            throw new RuntimeException("Unknown object");
        ObjectIdBoundary objectId = update.getObjectId();
        if (objectId != null && !objectId.getInternalObjectId().equals(internalObjectId))
            throw new RuntimeException("Cannot change object's id");
        UserIdWrapper newCreatedBy = update.getCreatedBy();
        if(newCreatedBy != null && !object.getCreatedBy().getUserId().equals(newCreatedBy.getUserId()))
            throw new RuntimeException("Cannot change object's creator");

        Map<String, Object> newDetails = update.getObjectDetails();
        Boolean newActive = update.getActive();
        String newType = update.getType();
        String newAlias = update.getAlias();

        if (newDetails != null)
            object.setObjectDetails(newDetails);
        if (newActive != null)
            object.setActive(newActive);
        if (newType != null)
            object.setType(newType);
        if (newAlias != null)
            object.setAlias(newAlias);

        return update;
    }

    @Override
    public ObjectBoundary getSpecificObject(@Value("${spring.application.name}") String objectSuperapp,
                                            String internalObjectId) {
        if (!objects.containsKey(internalObjectId))
            throw new RuntimeException("Object does not exist");

        return this.converter.toBoundary(objects.get(internalObjectId));
    }

    @Override
    public List<ObjectBoundary> getAllObjects() {
        return this.objects.values()
                .stream()
                .map(this.converter::toBoundary)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteAllObjects() {
        objects.clear();
    }
}
