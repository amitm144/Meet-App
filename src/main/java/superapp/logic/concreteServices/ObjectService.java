package superapp.logic.concreteServices;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import superapp.boundaries.object.ObjectBoundary;
import superapp.boundaries.object.ObjectIdBoundary;
import superapp.boundaries.user.UserIdBoundary;
import superapp.converters.ObjectConverter;
import superapp.dal.IdGeneratorRepository;
import superapp.dal.ObjectEntityRepository;
import superapp.data.IdGeneratorEntity;
import superapp.data.ObjectEntity;
import superapp.logic.AbstractService;
import superapp.logic.ObjectsService;
import superapp.util.EmailChecker;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ObjectService extends AbstractService implements ObjectsService {
    private ObjectEntityRepository objectRepository;
    private IdGeneratorRepository idGenerator;
    private ObjectConverter converter;

    @Autowired
    public ObjectService(ObjectConverter converter,
                         ObjectEntityRepository objectRepository,
                         IdGeneratorRepository idGenerator) {
        this.converter = converter;
        this.objectRepository = objectRepository;
        this.idGenerator = idGenerator;
    }

    @Override
    @Transactional
    public ObjectBoundary createObject(ObjectBoundary object) {
        String alias = object.getAlias();
        String type = object.getType(); // TODO: check type corresponds to future object types
        if (alias == null || type == null || alias.isBlank() || type.isBlank())
            throw new RuntimeException("Object alias and/or type must be specified");

        UserIdBoundary createdBy = object.getCreatedBy().getUserId();
        if (createdBy == null ||
                createdBy.getEmail() == null ||
                createdBy.getSuperapp() == null ||
                createdBy.getSuperapp().isEmpty() ||
                !EmailChecker.isValidEmail(createdBy.getEmail()))
            throw new RuntimeException("Invalid creating user details");

        Boolean active = object.getActive();
        if (active == null)
            active = false;

        IdGeneratorEntity helper = this.idGenerator.save(new IdGeneratorEntity());
        String objectId = helper.getId().toString();
        this.idGenerator.delete(helper);

        object.setObjectId(new ObjectIdBoundary(this.superappName, objectId));
        object.setActive(active);
        object.setCreationTimestamp(new Date());

        this.objectRepository.save(converter.toEntity(object));
        return object;
    }

    @Override
    @Transactional
    public ObjectBoundary updateObject(String objectSuperapp,
                                       String internalObjectId,
                                       ObjectBoundary update) {
        Optional<ObjectEntity> objectO = this.objectRepository.findByCompositeId(objectSuperapp, internalObjectId);
        if (objectO.isEmpty())
            throw new RuntimeException("Unknown object");

        ObjectEntity objectE = objectO.get();
        Map<String, Object> newDetails = update.getObjectDetails();
        Boolean newActive = update.getActive();
        String newType = update.getType(); // TODO: check type corresponds to future object types
        String newAlias = update.getAlias();

        if (newDetails != null)
            objectE.setObjectDetails(this.converter.detailsToString(newDetails));
        if (newActive != null)
            objectE.setActive(newActive);

        if (newType != null) {
            if (newType.isBlank())
                throw new RuntimeException("Object alias and/or type must be specified");
            else
                objectE.setType(newType);
        }

        if (newAlias != null) {
            if (newAlias.isBlank())
                throw new RuntimeException("Object alias and/or type must be specified");
            else
                objectE.setAlias(newAlias);
        }

        objectE = this.objectRepository.save(objectE);
        return this.converter.toBoundary(objectE);
    }

    @Override
    @Transactional(readOnly = true)
    public ObjectBoundary getSpecificObject(String objectSuperapp, String internalObjectId) {
        Optional<ObjectEntity> objectE = this.objectRepository.findByCompositeId(objectSuperapp, internalObjectId);
        if (objectE.isEmpty())
            throw new RuntimeException("Object does not exist");

        return this.converter.toBoundary(objectE.get());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ObjectBoundary> getAllObjects() {
        Iterable<ObjectEntity> objects = this.objectRepository.findAll();
        return StreamSupport
                .stream(objects.spliterator() , false)
                .map(this.converter::toBoundary)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteAllObjects() { this.objectRepository.deleteAll(); }
}
