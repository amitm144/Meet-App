package superapp.logic.concreteServices;

import superapp.boundaries.object.ObjectBoundary;
import superapp.boundaries.object.ObjectIdBoundary;
import superapp.converters.GroupConverter;
import superapp.converters.ObjectConverter;
import superapp.data.GroupEntity;
import superapp.data.ObjectEntity;
import superapp.logic.ObjectsService;
import superapp.util.wrappers.UserIdWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ObjectService implements ObjectsService {
    private Map<String, ObjectEntity> objects; // { object id : object }
    private ObjectConverter obecjtConverter;
    private GroupConverter groupConverter;
    @Autowired
    public ObjectService(ObjectConverter obecjtConverter, GroupConverter groupConverter) {
        this.obecjtConverter = obecjtConverter;
        this.groupConverter = groupConverter;
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
        ObjectEntity oe = obecjtConverter.toEntity(object);
        objects.put(object.getObjectId().getInternalObjectId(),oe);
        switch (oe.getType()){
            case "GroupEntity":{
                GroupEntity groupEntity = obecjtConverter.ToGroupEntity(oe);
                //TODO add to DB
                break;
            }

        }
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

        return this.obecjtConverter.toBoundary(objects.get(internalObjectId));
    }

    @Override
    public List<ObjectBoundary> getAllObjects() {
        return this.objects.values()
                .stream()
                .map(this.obecjtConverter::toBoundary)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteAllObjects() {
        objects.clear();
    }
}
