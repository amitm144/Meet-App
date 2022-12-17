package superapp.logic.concreteServices;

import superapp.boundaries.object.ObjectBoundary;
import superapp.boundaries.object.ObjectIdBoundary;
import superapp.converters.ObjectConverter;
import superapp.dal.ObjectDetailsEntityRepository;
import superapp.dal.ObjectEntityRepository;
import superapp.data.ObjectDetailsEntity;
import superapp.data.ObjectEntity;
import superapp.logic.ObjectsService;
import superapp.util.wrappers.UserIdWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.lang.Integer.parseInt;

@Service
public class ObjectService implements ObjectsService {

    @Autowired
    private ObjectEntityRepository objectRepository;
    @Autowired
    private ObjectDetailsEntityRepository objectDetailsRepository;
    private ObjectConverter converter;

    @Autowired
    public ObjectService(ObjectConverter converter) {
        this.converter = converter;
    }

    @Override
//    @Transactional(readOnly = false)
    public ObjectBoundary createObject(ObjectBoundary object) {
        String objectId = object.getObjectId().getInternalObjectId();
        Optional<ObjectEntity> objectE = this.objectRepository.findById(objectId);
        if (objectE.isPresent())
            throw new RuntimeException("Object already exists");

        if (objectId.equals("") || objectId.equals("null")) {
            object.getObjectId().setInternalObjectId(String.valueOf(
                    parseInt(this.objectRepository.findLastId()) + 1));
        }
        String superapp = object.getObjectId().getSuperapp();
        if (superapp == null || superapp.equals("null"))
            throw new RuntimeException("Superapp name cannot be empty");

        AtomicInteger number = new AtomicInteger();
        object.getObjectDetails().forEach((key, value) -> {
            String objDetailsId = this.objectDetailsRepository.findLastId();
            if(objDetailsId != null) {
                number.set(parseInt(objDetailsId) + 1);
            }
            ObjectDetailsEntity objDetails = new ObjectDetailsEntity(
                    Integer.toString(number.get()),
                    key,
                    value.toString(),
                    value.toString());
            this.objectDetailsRepository.save(objDetails);
        });
        object.setCreationTimestamp(new Date());
        this.objectRepository.save(converter.toEntity(object));


        return object;
    }

    @Override
//    @Transactional(readOnly = false)
    public ObjectBoundary updateObject(String objectSuperapp,
                                       String internalObjectId,
                                       ObjectBoundary update) {
        Optional<ObjectEntity> objectE = this.objectRepository.findById(internalObjectId);
        if (objectE.isEmpty() || !objectE.get().getSuperapp().equals(objectSuperapp))
            throw new RuntimeException("Unknown object");
        UserIdWrapper newCreatedBy = update.getCreatedBy();
        if(newCreatedBy != null && !objectE.get().getCreatedBy().getUserId().equals(newCreatedBy.getUserId()))
            throw new RuntimeException("Cannot change object's creator");

        Map<String, Object> newDetails = update.getObjectDetails();
        Boolean newActive = update.getActive();
        String newType = update.getType();
        String newAlias = update.getAlias();

        if (newDetails != null)
            objectE.get().setObjectDetails(newDetails);
        if (newActive != null)
            objectE.get().setActive(newActive);
        if (newType != null)
            objectE.get().setType(newType);
        if (newAlias != null)
            objectE.get().setAlias(newAlias);
        ObjectEntity newObj = objectE.get();
        this.objectRepository.save(newObj);
        return update;
    }

    @Override
//    @Transactional(readOnly = true)
    public ObjectBoundary getSpecificObject(@Value("${spring.application.name}") String objectSuperapp,
                                            String internalObjectId) {
        Optional<ObjectEntity> objectE = this.objectRepository.findById(internalObjectId);
        if (objectE.isEmpty())
            throw new RuntimeException("Object does not exist");

        return this.converter.toBoundary(objectE.get());
    }

    @Override
//    @Transactional(readOnly = true)
    public List<ObjectBoundary> getAllObjects() {
        Iterable<ObjectEntity> objects = this.objectRepository.findAll();
        return StreamSupport
                .stream(objects.spliterator() , false)
                .map(this.converter::toBoundary)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteAllObjects() {
        this.objectRepository.deleteAll();
    }
}
