package superapp.logic.concreteServices;

import superapp.boundaries.object.ObjectBoundary;
import superapp.boundaries.object.ObjectIdBoundary;
import superapp.boundaries.user.UserIdBoundary;
import superapp.converters.ObjectConverter;
import superapp.data.ObjectEntity;
import superapp.logic.AbstractService;
import superapp.logic.ObjectsService;
import superapp.util.wrappers.UserIdWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ObjectService extends AbstractService implements ObjectsService {
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
        ObjectIdBoundary id = object.getObjectId();
        if (id != null &&
                id.getSuperapp() != null &&
                !isValidSuperapp(id.getSuperapp())) {
            throw new RuntimeException("Unknown superapp or superapp name is empty");
        }
        if (id != null && objects.containsKey(id.getInternalObjectId()))
            throw new RuntimeException("Object already exists");

        UserIdBoundary createdBy = object.getCreatedBy() != null ? object.getCreatedBy().getUserId() : null;
        if (createdBy == null || !isValidSuperapp(createdBy.getSuperapp()))
            throw new RuntimeException("Unknown user");

        String internalId = String.valueOf(this.objects.size() + 1);
        object.setObjectId(new ObjectIdBoundary(
                this.superappName,
                internalId
        ));
        object.setCreationTimestamp(new Date());
        /*
         TODO:
          check for valid active, type and alias
          TBD - set default values or throw runtime exception
          if empty objectDetails, set to be empty map
          IMO, value setting should be in the entity it self.
         */
        objects.put(internalId, converter.toEntity(object));
        return object;
    }

    @Override
    public ObjectBoundary updateObject(String objectSuperapp,
                                       String internalObjectId,
                                       ObjectBoundary update) {
        ObjectIdBoundary id = update.getObjectId();
        if (id != null) {
            if (!isValidSuperapp(id.getSuperapp()))
                throw new RuntimeException("Incorrect superapp");
            if (!id.getInternalObjectId().equals(internalObjectId))
                throw new RuntimeException("Cannot change object's id");
        }

        ObjectEntity object = this.objects.get(internalObjectId);
        if (object == null || !object.getSuperapp().equals(objectSuperapp))
            throw new RuntimeException("Unknown object");

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
    public ObjectBoundary getSpecificObject(String objectSuperapp,
                                            String internalObjectId) {
        if (!isValidSuperapp(objectSuperapp))
            throw new RuntimeException("Incorrect superapp");

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
