package superapp.logic.concreteServices;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import superapp.boundaries.object.SuperAppObjectBoundary;
import superapp.boundaries.object.SuperAppObjectIdBoundary;
import superapp.boundaries.user.UserIdBoundary;
import superapp.converters.SuperAppObjectConverter;
import superapp.dal.IdGeneratorRepository;
import superapp.dal.SuperAppObjectEntityRepository;
import superapp.dal.UserEntityRepository;
import superapp.data.IdGeneratorEntity;
import superapp.data.SuperAppObjectEntity;
import superapp.data.SuperAppObjectEntity.SuperAppObjectId;
import superapp.data.UserEntity;
import superapp.logic.AbstractService;
import superapp.logic.AdvancedSuperAppObjectsService;
import superapp.util.exceptions.CannotProcessException;
import superapp.util.exceptions.InvalidInputException;
import superapp.util.exceptions.NotFoundException;
import superapp.util.EmailChecker;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static superapp.data.UserRole.SUPERAPP_USER;


@Service
public class SuperAppObjectService extends AbstractService implements AdvancedSuperAppObjectsService {
    public SuperAppObjectEntityRepository objectRepository;
    public UserEntityRepository userRepository;
    private IdGeneratorRepository idGenerator;
    public SuperAppObjectConverter converter;

    @Autowired
    public SuperAppObjectService(SuperAppObjectConverter converter,
                                 SuperAppObjectEntityRepository objectRepository,
                                 IdGeneratorRepository idGenerator) {
        this.converter = converter;
        this.objectRepository = objectRepository;
        this.idGenerator = idGenerator;
    }

    @Override
    @Transactional
    public SuperAppObjectBoundary createObject(SuperAppObjectBoundary object) {
        String alias = object.getAlias();
        String type = object.getType(); // TODO: check type corresponds to future object types
        if (alias == null || type == null || alias.isBlank() || type.isBlank())
            throw new InvalidInputException("Object alias and/or type must be specified");

        UserIdBoundary createdBy = object.getCreatedBy().getUserId();
        if (createdBy == null ||
                createdBy.getEmail() == null ||
                createdBy.getSuperapp() == null ||
                createdBy.getSuperapp().isEmpty() ||
                !EmailChecker.isValidEmail(createdBy.getEmail()))
            throw new InvalidInputException("Invalid creating user details");

        Boolean active = object.getActive();
        if (active == null)
            active = false;

        IdGeneratorEntity helper = this.idGenerator.save(new IdGeneratorEntity());
        String objectId = helper.getId().toString();
        this.idGenerator.delete(helper);

        object.setObjectId(new SuperAppObjectIdBoundary(this.superappName, objectId));
        object.setActive(active);
        object.setCreationTimestamp(new Date());

        this.objectRepository.save(converter.toEntity(object));
        return object;
    }

    @Override
    @Deprecated
    @Transactional
    public SuperAppObjectBoundary updateObject(String objectSuperapp,
                                               String internalObjectId,
                                               SuperAppObjectBoundary update)
    {
        //TODO need to change exception
        throw new NotFoundException("Method is Dperecated");
    }

    @Override
    @Transactional
    public SuperAppObjectBoundary updateObject(String objectSuperapp,
                                               String internalObjectId,
                                               SuperAppObjectBoundary update,String userSuperapp,String email) {
        if (!isSuperappUser(userSuperapp, email))
            //TODO need to change exception
            throw new NotFoundException("Error: Only SUPERAPP_USER is allowed to access this method.");

        Optional<SuperAppObjectEntity> objectO =
                this.objectRepository.findById(new SuperAppObjectId(objectSuperapp, internalObjectId));
        if (objectO.isEmpty())
            throw new NotFoundException("Unknown object");

        SuperAppObjectEntity objectE = objectO.get();
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
                throw new InvalidInputException("Object alias and/or type must be specified");
            else
                objectE.setType(newType);
        }

        if (newAlias != null) {
            if (newAlias.isBlank())
                throw new InvalidInputException("Object alias and/or type must be specified");
            else
                objectE.setAlias(newAlias);
        }

        objectE = this.objectRepository.save(objectE);
        return this.converter.toBoundary(objectE);
    }
    @Override
    @Deprecated
    @Transactional
    public void bindNewChild(String parentSuperapp, String parentObjectId, SuperAppObjectIdBoundary newChild) {
        //TODO need to change exception
        throw new NotFoundException("Method is Dperecated");
    }
    @Override
    @Transactional
    public void bindNewChild(String parentSuperapp, String parentObjectId, SuperAppObjectIdBoundary newChild,String userSuperapp, String email) {
        if (!isSuperappUser(userSuperapp, email))
            throw new NotFoundException("Error: Only SUPERAPP_USER is allowed to access this method.");
        SuperAppObjectEntity parent = this.objectRepository
                .findById(new SuperAppObjectId(parentSuperapp, parentObjectId))
                .orElseThrow(() -> new NotFoundException("Cannot find parent object"));
        SuperAppObjectEntity child = this.objectRepository
                .findById(this.converter.idToEntity(newChild))
                .orElseThrow(() -> new NotFoundException("Cannot find children object"));

        if (parent.addChild(child) && child.addParent(parent)) {
            this.objectRepository.save(parent);
            this.objectRepository.save(child);
        } else
            throw new CannotProcessException("Failed to update parent or child object");
    }

    @Override
    @Deprecated
    @Transactional(readOnly = true)
    public SuperAppObjectBoundary getSpecificObject(String objectSuperapp, String internalObjectId) {
        //TODO need to change exception
        throw new NotFoundException("Method is Dperecated");
    }

    @Override
    @Transactional(readOnly = true)
    public SuperAppObjectBoundary getSpecificObject(String objectSuperapp, String internalObjectId, String userSuperapp, String email) {
        if (!isSuperappUser(userSuperapp, email))
            throw new NotFoundException("Error: Only SUPERAPP_USER is allowed to access this method.");

        Optional<SuperAppObjectEntity> objectE =
                this.objectRepository.findById(new SuperAppObjectId(objectSuperapp, internalObjectId));
        if (objectE.isEmpty())
            throw new NotFoundException("Object does not exist");

        return this.converter.toBoundary(objectE.get());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SuperAppObjectBoundary> getChildren(String objectSuperapp, String internalObjectId,String userSuperapp, String email, int size, int page) {
        if (!isSuperappUser(userSuperapp, email))
            throw new NotFoundException("Error: Only SUPERAPP_USER is allowed to access this method.");

        SuperAppObjectEntity parent = this.objectRepository
                .findById(new SuperAppObjectId(objectSuperapp, internalObjectId), PageRequest.of(page, size, Sort.Direction.DESC, "objectSuperapp", "internalObjectId"))
                .orElseThrow(() -> new NotFoundException("Cannot find parent object"));

        return parent
                .getChildren()
                .stream()
                .map(this.converter::toBoundary)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SuperAppObjectBoundary> getParents(String objectSuperapp, String internalObjectId,String userSuperapp, String email, int size, int page) {
        SuperAppObjectEntity object = this.objectRepository
                .findById(new SuperAppObjectId(objectSuperapp, internalObjectId))
                .orElseThrow(() -> new NotFoundException("Cannot find requested object"));

        return object
                .getParents()
                .stream()
                .map(this.converter::toBoundary)
                .collect(Collectors.toList());
    }

    @Override
    @Deprecated
    @Transactional(readOnly = true)
    public List<SuperAppObjectBoundary> getAllObjects() {
        throw new InvalidInputException("Method is Dperecated");
    }

    @Override
    @Transactional(readOnly = true)
    public List<SuperAppObjectBoundary> getAllObjects(String userSuperapp, String email, int size, int page) {
        if (!isSuperappUser(userSuperapp, email))
            throw new NotFoundException("Error: Only SUPERAPP_USER is allowed to access this method.");

        return this.objectRepository.findAll(PageRequest.of(page, size, Sort.Direction.DESC,
                        "userSuperapp", "userEmail"))
                    .stream()
                    .map(this.converter::toBoundary)
                    .collect(Collectors.toList());
    }

    @Override
    public List<SuperAppObjectBoundary> SearchObjectsByType(String type, String userSuperapp, String email, int size, int page) {
        if (!isSuperappUser(userSuperapp, email))
            //TODO need to change exception
            throw new NotFoundException("Error: Only SUPERAPP_USER is allowed to access this method.");

        return this.objectRepository.findByType(type, userSuperapp, email, PageRequest.of(page, size, Sort.Direction.DESC, "objectId"))
                .stream()
                .map(this.converter::toBoundary)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<SuperAppObjectBoundary> SearchObjectsByExactAlias(String alias, String userSuperapp, String email, int size, int page) {
        if (!isSuperappUser(userSuperapp, email))
            //TODO need to change exception
            throw new NotFoundException("Error: Only SUPERAPP_USER is allowed to access this method.");

        return this.objectRepository.findByAlias(alias, userSuperapp,email, PageRequest.of(page, size, Sort.Direction.DESC, "objectId"))
                .stream()
                .map(this.converter::toBoundary)
                .collect(Collectors.toList());
    }
    @Override
    @Transactional
    public void deleteAllObjects() {
        this.objectRepository.deleteAll();
    }

    @Override
    @Transactional
    public List<SuperAppObjectBoundary> SearchObjectsByExactAliasContainingText(String text, String userSuperapp, String email, int size, int page)
    {
        if (!isSuperappUser(userSuperapp, email))
            //TODO need to change exception
            throw new NotFoundException("Error: Only SUPERAPP_USER is allowed to access this method.");
        return null;
    }

    private boolean isSuperappUser(String userSuperapp, String email) {
        Optional<UserEntity> userE = userRepository.findById(new UserEntity.UserPK(userSuperapp, email));
        if (userE.isPresent() && userE.get().getRole().equals(SUPERAPP_USER))
            return true;
        return false;
    }
}
