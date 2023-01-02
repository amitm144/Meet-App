package superapp.logic.concreteServices;

import org.springframework.data.domain.PageRequest;
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
import superapp.data.*;
import superapp.logic.AbstractService;
import superapp.logic.AdvancedSuperAppObjectsService;
import superapp.util.exceptions.CannotProcessException;
import superapp.util.exceptions.ForbiddenInsteadException;
import superapp.util.exceptions.InvalidInputException;
import superapp.util.exceptions.NotFoundException;
import superapp.util.EmailChecker;
import static superapp.data.UserRole.*;
import static superapp.util.ControllersConstants.DEFAULT_SORTING_DIRECTION;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SuperAppObjectService extends AbstractService implements AdvancedSuperAppObjectsService {
    private SuperAppObjectEntityRepository objectRepository;
    private UserEntityRepository userRepository;
    private IdGeneratorRepository idGenerator;
    private SuperAppObjectConverter converter;

    @Autowired
    public SuperAppObjectService(SuperAppObjectConverter converter,
                                 SuperAppObjectEntityRepository objectRepository,
                                 IdGeneratorRepository idGenerator,
                                 UserEntityRepository userRepository) {
        this.converter = converter;
        this.objectRepository = objectRepository;
        this.idGenerator = idGenerator;
        this.userRepository = userRepository;
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
                                               SuperAppObjectBoundary update) {
        throw new ForbiddenInsteadException("Method is Dperecated");
    }
    @Override
    @Deprecated
    @Transactional
    public void bindNewChild(String parentSuperapp, String parentObjectId, SuperAppObjectIdBoundary newChild) {
        throw new NotFoundException("Method is Dperecated");
    }

    @Override
    @Deprecated
    @Transactional(readOnly = true)
    public SuperAppObjectBoundary getSpecificObject(String objectSuperapp, String internalObjectId) {
        throw new NotFoundException("Method is Dperecated");
    }

    @Override
    @Deprecated
    @Transactional(readOnly = true)
    public List<SuperAppObjectBoundary> getAllObjects() {
        throw new NotFoundException("Method is Dperecated");
    }

    @Override
    @Deprecated
    @Transactional
    public void deleteAllObjects() {
        throw new NotFoundException("Method is Dperecated");
    }


    @Override
    @Transactional
    public SuperAppObjectBoundary updateObject(String objectSuperapp,
                                               String internalObjectId,
                                               SuperAppObjectBoundary update,String userSuperapp,String email) {
        UserPK userId = new UserPK(userSuperapp, email);
        if(!this.isValidUserCredentials(userId, SUPERAPP_USER, this.userRepository))
            throw new ForbiddenInsteadException("Error: Only MINIAPP_USERS and SUPERAPP_USERS able to access here");

        Optional<SuperAppObjectEntity> objectO =
                this.objectRepository.findById(new SuperappObjectPK(objectSuperapp, internalObjectId));
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
    @Transactional
    public void bindNewChild(String parentSuperapp, String parentObjectId,
                             SuperAppObjectIdBoundary newChild,
                             String userSuperapp, String email) {
        UserPK userId = new UserPK(userSuperapp, email);
        if(!this.isValidUserCredentials(userId, SUPERAPP_USER, this.userRepository))
            throw new ForbiddenInsteadException("Error:SUPERAPP_USERS able to access here");

        SuperAppObjectEntity parent = this.objectRepository
                .findById(new SuperappObjectPK(parentSuperapp, parentObjectId))
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
    @Transactional(readOnly = true)
    public SuperAppObjectBoundary getSpecificObject(String objectSuperapp, String internalObjectId, String userSuperapp, String email) {
        UserPK userId = new UserPK(userSuperapp, email);
        Optional<SuperAppObjectEntity> objectE = this.objectRepository.findById(new SuperappObjectPK(objectSuperapp, internalObjectId));
        if (objectE.isEmpty())
            throw new NotFoundException("Object does not exist");
        if (this.isValidUserCredentials(userId, SUPERAPP_USER, this.userRepository)) {
            return this.converter.toBoundary(objectE.get());
        } else if (this.isValidUserCredentials(userId, MINIAPP_USER, this.userRepository)){
             if (!objectE.get().getActive())
                throw new CannotProcessException("cannot accses to an object that is inactive");
        return this.converter.toBoundary(objectE.get());
     }
        throw new ForbiddenInsteadException("Error: Only MINIAPP_USERS and SUPERAPP_USERS able to access here");
    }

    @Override
    @Transactional(readOnly = true)
    public List<SuperAppObjectBoundary> getChildren(String objectSuperapp, String internalObjectId,
                                                    String userSuperapp, String email,
                                                    int size, int page) {
        UserPK userId = new UserPK(userSuperapp, email);
        PageRequest pageReq =PageRequest.of(page, size, DEFAULT_SORTING_DIRECTION, "superapp", "objectId");
        if(this.isValidUserCredentials(userId, SUPERAPP_USER, this.userRepository))
            return getChildrenRepoSearch(pageReq,internalObjectId,userSuperapp ,true);

        else if(this.isValidUserCredentials(userId, MINIAPP_USER, this.userRepository))
            return getChildrenRepoSearch(pageReq,internalObjectId,userSuperapp,false);

        throw new ForbiddenInsteadException("Error: Only MINIAPP_USERS and SUPERAPP_USERS able to access here");
    }

    @Override
    @Transactional(readOnly = true)
    public List<SuperAppObjectBoundary> getParents(String objectSuperapp, String internalObjectId,String userSuperapp,
                                                   String email, int size, int page) {
        UserPK userId = new UserPK(userSuperapp, email);
        PageRequest pageReq = PageRequest.of(page, size, DEFAULT_SORTING_DIRECTION, "superapp", "objectId");

        if(this.isValidUserCredentials(userId, SUPERAPP_USER, this.userRepository))
            return getParentRepoSearch(pageReq,internalObjectId,objectSuperapp,true);

        else if(this.isValidUserCredentials(userId, MINIAPP_USER, this.userRepository))
            return getParentRepoSearch(pageReq,internalObjectId,objectSuperapp,false);

        throw new ForbiddenInsteadException("Error: Only MINIAPP_USERS and SUPERAPP_USERS able to access here");

    }
    private List<SuperAppObjectBoundary> getParentRepoSearch(PageRequest pageReq, String internalObjectId, String objectSuperapp, boolean isSuperAppUser) {
        return this.objectRepository.findAll(pageReq)
                .stream()
                .filter(obj -> obj.getObjectId().equals(internalObjectId) && obj.getSuperapp().equals(objectSuperapp))
                .map(SuperAppObjectEntity::getParents)
                .flatMap(superAppObjectEntities -> superAppObjectEntities.stream().map(this.converter::toBoundary))
                .filter(object -> object.getActive()|| isSuperAppUser)// if MiniappUser get only active
                .collect(Collectors.toList());
    }
    private List<SuperAppObjectBoundary> getChildrenRepoSearch(PageRequest pageReq, String internalObjectId, String objectSuperapp, boolean isSuperAppUser) {
        return this.objectRepository.findAll(pageReq)
                .stream()
                .filter(obj -> obj.getObjectId().equals(internalObjectId) && obj.getSuperapp().equals(objectSuperapp))
                .map(SuperAppObjectEntity::getChildren)
                .flatMap(superAppObjectEntities -> superAppObjectEntities.stream().map(this.converter::toBoundary))
                .filter(object -> object.getActive()|| isSuperAppUser)// if MiniappUser get only active
                .collect(Collectors.toList());
    }
    @Override
    @Transactional(readOnly = true)
    public List<SuperAppObjectBoundary> getAllObjects(String userSuperapp, String email, int size, int page) {
        UserPK userId = new UserPK(userSuperapp, email);
        PageRequest pageReq = PageRequest.of(page, size, DEFAULT_SORTING_DIRECTION,
                "userSuperapp", "userEmail");
        if (this.isValidUserCredentials(userId, SUPERAPP_USER, this.userRepository))
            return findAllObjectsRepoSearch(pageReq,true);

        else if(this.isValidUserCredentials(userId, MINIAPP_USER, this.userRepository))
            return findAllObjectsRepoSearch(pageReq,false);

        throw new ForbiddenInsteadException("Error: Only MINIAPP_USERS and SUPERAPP_USERS able to access here");
    }
    private List<SuperAppObjectBoundary> findAllObjectsRepoSearch(PageRequest pageReq, boolean isSuperAppUser) {
        return this.objectRepository.findAll(pageReq)
                .stream()
                .map(this.converter::toBoundary)
                .filter(object -> object.getActive()|| isSuperAppUser)// if MiniappUser get only active
                .collect(Collectors.toList());
    }
    @Override
    public List<SuperAppObjectBoundary> SearchObjectsByType(String type, String userSuperapp, String email, int size, int page) {
        UserPK userId = new UserPK(userSuperapp, email);
        PageRequest pageReq = PageRequest.of(page, size, DEFAULT_SORTING_DIRECTION, "objectId");
        if( isValidUserCredentials(userId, SUPERAPP_USER, this.userRepository))
            return findAllObjectsByTypeRepoSearch(pageReq,type,true);

        else if (isValidUserCredentials(userId, MINIAPP_USER, this.userRepository))
            return findAllObjectsByTypeRepoSearch(pageReq,type,false);

        throw new ForbiddenInsteadException("Error: Only MINIAPP_USERS and SUPERAPP_USERS able to access here");
    }
    private List<SuperAppObjectBoundary> findAllObjectsByTypeRepoSearch(PageRequest pageReq, String type, boolean isSuperAppUser){
        return this.objectRepository.findByType(type,pageReq)
                .stream()
                .map(this.converter::toBoundary)
                .filter(object -> object.getActive()|| isSuperAppUser)// if MiniappUser get only active
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<SuperAppObjectBoundary> SearchObjectsByExactAlias(String alias, String userSuperapp, String email, int size, int page) {
        UserPK userId = new UserPK(userSuperapp, email);
        PageRequest pageReq = PageRequest.of(page, size, DEFAULT_SORTING_DIRECTION, "objectId");
        if(this.isValidUserCredentials(userId, SUPERAPP_USER, this.userRepository))
            return findByAliasRepoSearch(pageReq,alias,true);

        else if(this.isValidUserCredentials(userId, MINIAPP_USER, this.userRepository))
            return findByAliasRepoSearch(pageReq,alias,false);

        throw new ForbiddenInsteadException("Error: Only MINIAPP_USERS and SUPERAPP_USERS able to access here");
    }
    private List<SuperAppObjectBoundary> findByAliasRepoSearch(PageRequest pageReq, String alias, boolean isSuperAppUser){
        return this.objectRepository.findByAlias(alias,pageReq)
                .stream()
                .map(this.converter::toBoundary)
                .filter(object -> object.getActive() || isSuperAppUser)// if MiniappUser get only active
                .collect(Collectors.toList());
    }
    @Override
    @Transactional
    public List<SuperAppObjectBoundary> SearchObjectsByExactAliasContainingText(String text, String userSuperapp, String email, int size, int page)
    {
        UserPK userId = new UserPK(userSuperapp, email);
        PageRequest pageReq = PageRequest.of(page, size, DEFAULT_SORTING_DIRECTION, "objectId");
        if(this.isValidUserCredentials(userId, SUPERAPP_USER, this.userRepository))
            return findByAliasContainingRepoSearch(pageReq,text,true);

        else if(this.isValidUserCredentials(userId, MINIAPP_USER, this.userRepository))
            return findByAliasContainingRepoSearch(pageReq,text,false);
        else
            throw new ForbiddenInsteadException("Error: Only MINIAPP_USERS and SUPERAPP_USERS able to access here");

    }
    private List<SuperAppObjectBoundary> findByAliasContainingRepoSearch(PageRequest pageReq, String text, boolean isSuperAppUser){
        return this.objectRepository
                .findByAliasContaining(text, pageReq)
                .stream()
                .filter(object -> object.getActive() || isSuperAppUser) // if MiniappUser get only active
                .map(this.converter::toBoundary)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteAllObjects(String userSuperapp, String email) {
        UserPK userId = new UserPK(userSuperapp, email);
        if(isValidUserCredentials(userId, ADMIN, this.userRepository))
            this.objectRepository.deleteAll();
        else
            throw new ForbiddenInsteadException("Error: Only SUPERAPP_USERS able to access here");

    }
}
