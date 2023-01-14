package superapp.logic.concreteServices;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
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
import superapp.logic.MiniAppServices;
import superapp.util.exceptions.CannotProcessException;
import superapp.util.exceptions.ForbbidenOperationException;
import superapp.util.exceptions.InvalidInputException;
import superapp.util.exceptions.NotFoundException;
import superapp.util.EmailChecker;

import static superapp.data.ObjectTypes.*;
import static superapp.data.Timeframes.isValidTimeframes;
import static superapp.data.UserRole.*;
import static superapp.util.Constants.*;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SuperAppObjectService extends AbstractService implements AdvancedSuperAppObjectsService {
    private ApplicationContext context;
    private MiniAppServices miniAppService;
    private SuperAppObjectConverter converter;
    private IdGeneratorRepository idGenerator;
    private SuperAppObjectEntityRepository objectRepository;
    private UserEntityRepository userRepository;
    private Log logger = LogFactory.getLog(MiniAppCommandService.class);

    @Autowired
    public SuperAppObjectService(SuperAppObjectConverter converter, UserEntityRepository userRepository,
                                 SuperAppObjectEntityRepository objectRepository, IdGeneratorRepository idGenerator,
                                 ApplicationContext context) {
        this.converter = converter;
        this.objectRepository = objectRepository;
        this.idGenerator = idGenerator;
        this.userRepository = userRepository;
        this.context = context;
    }

    @Override
    @Transactional
    public SuperAppObjectBoundary createObject(SuperAppObjectBoundary object) {
        String alias = object.getAlias();
        String type = object.getType();
        if (alias == null || type == null || alias.isBlank() || type.isBlank()) {
            this.logger.debug("in createObject func - Object alias and/or type must be specified");
            throw new InvalidInputException("Object alias and/or type must be specified");
        }
        UserIdBoundary createdBy = object.getCreatedBy().getUserId();
        if (createdBy == null ||
                createdBy.getEmail() == null ||
                createdBy.getSuperapp() == null ||
                createdBy.getSuperapp().isEmpty() ||
                !EmailChecker.isValidEmail(createdBy.getEmail())) {
            this.logger.debug("in createObject func - Invalid creating user details");
            throw new InvalidInputException("Invalid creating user details");
        }

        if (!this.isValidUserCredentials(new UserPK(createdBy.getSuperapp(),createdBy.getEmail()),
                SUPERAPP_USER, this.userRepository)) {
            this.logger.debug("in createObject func - %s".formatted(SUPERAPP_USER_ONLY_EXCEPTION));
            throw new ForbbidenOperationException(SUPERAPP_USER_ONLY_EXCEPTION);
        }


        Boolean active = object.getActive();
        if (active == null)
            active = false;

        IdGeneratorEntity helper = this.idGenerator.save(new IdGeneratorEntity());
        String objectId = helper.getId().toString();
        this.idGenerator.delete(helper);

        object.setObjectId(new SuperAppObjectIdBoundary(this.superappName, objectId));
        object.setActive(active);
        object.setCreationTimestamp(new Date());
        try {
            this.handleObject(object); // will handle any unknown object type by 400 - Bad request.
        } catch (InvalidInputException e) {
            object.setActive(false);
            this.logger.error("in createObject func - %s".formatted(e.getMessage()));
            throw new InvalidInputException(e.getMessage());
        } finally {
            this.objectRepository.save(converter.toEntity(object));
        }
        this.logger.info("Created object successfully");
        return object;
    }

    @Override
    @Deprecated
    @Transactional
    public SuperAppObjectBoundary updateObject(String objectSuperapp,
                                               String internalObjectId,
                                               SuperAppObjectBoundary update) {
        this.logger.debug("in updateObject func, - %s".formatted(DEPRECATED_EXCEPTION));
        throw new ForbbidenOperationException(DEPRECATED_EXCEPTION);
    }

    @Override
    @Deprecated
    @Transactional
    public void bindNewChild(String parentSuperapp, String parentObjectId, SuperAppObjectIdBoundary newChild) {
        this.logger.debug("in bindNewChild func, - %s".formatted(DEPRECATED_EXCEPTION));
        throw new ForbbidenOperationException(DEPRECATED_EXCEPTION);
    }

    @Override
    @Deprecated
    @Transactional(readOnly = true)
    public SuperAppObjectBoundary getSpecificObject(String objectSuperapp, String internalObjectId) {
        this.logger.debug("in getSpecificObject func, - %s".formatted(DEPRECATED_EXCEPTION));
        throw new ForbbidenOperationException(DEPRECATED_EXCEPTION);
    }

    @Override
    @Deprecated
    @Transactional(readOnly = true)
    public List<SuperAppObjectBoundary> getAllObjects() {
        this.logger.debug("in getAllObjects func, - %s".formatted(DEPRECATED_EXCEPTION));
        throw new ForbbidenOperationException(DEPRECATED_EXCEPTION);
    }

    @Override
    @Deprecated
    @Transactional
    public void deleteAllObjects() {
        this.logger.debug("in deleteAllObjects func, - %s".formatted(DEPRECATED_EXCEPTION));
        throw new ForbbidenOperationException(DEPRECATED_EXCEPTION);
    }

    @Override
    @Transactional
    public SuperAppObjectBoundary updateObject(String objectSuperapp, String internalObjectId,
                                               SuperAppObjectBoundary update, String userSuperapp, String email) {
        UserPK userId = new UserPK(userSuperapp, email);
        if(!this.isValidUserCredentials(userId, SUPERAPP_USER, this.userRepository)) {
            this.logger.debug("in updateObject func, - %s".formatted(SUPERAPP_MINIAPP_USERS_ONLY_EXCEPTION));
            throw new ForbbidenOperationException(SUPERAPP_USER_ONLY_EXCEPTION);
        }

        Optional<SuperAppObjectEntity> objectO =
                this.objectRepository.findById(new SuperappObjectPK(objectSuperapp, internalObjectId));
        if (objectO.isEmpty()) {
            this.logger.debug("in updateObject func, Unknown Object");
            throw new NotFoundException("Unknown object");
        }
        SuperAppObjectEntity objectE = objectO.get();
        Map<String, Object> newDetails = update.getObjectDetails();
        Boolean newActive = update.getActive();
        String newType = update.getType();
        String newAlias = update.getAlias();

        if (newDetails != null)
            objectE.setObjectDetails(this.converter.detailsToString(newDetails));
        if (newActive != null)
            objectE.setActive(newActive);

        if (newType != null) {
            if (newType.isBlank()) {
                this.logger.debug("in updateObject func, Object alias and/or type must be specified");
                throw new InvalidInputException("Object alias and/or type must be specified");
            }
            else if (!isValidObjectType(newType)) {
                this.logger.debug("in updateObject func, Unknown Object type");
                throw new InvalidInputException("Unknown object type");
            }
            else
                objectE.setType(newType);
        }

        if (newAlias != null) {
            if (newAlias.isBlank()) {
                this.logger.debug("in updateObject func, Object alias and/or type must be specified");
                throw new InvalidInputException("Object alias and/or type must be specified");
            }
            else
                objectE.setAlias(newAlias);
        }
        SuperAppObjectBoundary result = this.converter.toBoundary(objectE);
        /*
            handleObject will handle any unknown object type by 400 - Bad request.
            if object details after update doesn't fit into miniapp restrictions, an exception will be thrown as well
        */
        this.handleObject(result);
        this.objectRepository.save(objectE);
        this.logger.info("Updated super app object successfully");
        return result;
    }

    @Override
    @Transactional
    public void bindNewChild(String parentSuperapp, String parentObjectId,
                             SuperAppObjectIdBoundary newChild, String userSuperapp, String email) {
        UserPK userId = new UserPK(userSuperapp, email);
        if(!this.isValidUserCredentials(userId, SUPERAPP_USER, this.userRepository)) {
            this.logger.debug("in bindNewChild func, %s".formatted(SUPERAPP_USER_ONLY_EXCEPTION));
            throw new ForbbidenOperationException(SUPERAPP_USER_ONLY_EXCEPTION);
        }

        SuperAppObjectEntity parent = this.objectRepository
                .findById(new SuperappObjectPK(parentSuperapp, parentObjectId))
                .orElseThrow(() -> {
                    this.logger.debug("in bindNewChild func, Cannot find parent object");
                    return new NotFoundException("Cannot find parent object");
                });
        SuperAppObjectEntity child = this.objectRepository
                .findById(this.converter.idToEntity(newChild))
                .orElseThrow(() -> {
                    this.logger.debug("in bindNewChild func, Cannot find children object");
                    return new NotFoundException("Cannot find children object");
                });

        this.handleObjectBinding(parent, child, userId); // handle child appropriately if is miniapp object that has limitations
        if (parent.addChild(child) && child.addParent(parent)) {
            this.objectRepository.save(parent);
            this.objectRepository.save(child);
            this.logger.info("Bind new Child successfully");
        } else {
            this.logger.error("in bindNewChild func, Failed to update parent or child object");
            throw new CannotProcessException("Failed to update parent or child object");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public SuperAppObjectBoundary getSpecificObject(String objectSuperapp, String internalObjectId,
                                                    String userSuperapp, String email) {
        UserPK userId = new UserPK(userSuperapp, email);
        Optional<SuperAppObjectEntity> objectE = this.objectRepository.findById(new SuperappObjectPK(objectSuperapp, internalObjectId));

        if (objectE.isEmpty()) {
            this.logger.debug("in getSpecificObject func, Object does not exist");
            throw new NotFoundException("Object does not exist");
        }
        if (this.isValidUserCredentials(userId, SUPERAPP_USER, this.userRepository))
            return this.converter.toBoundary(objectE.get());

        else if (this.isValidUserCredentials(userId, MINIAPP_USER, this.userRepository)) {
             if (!objectE.get().getActive()) {
                 this.logger.debug("in getSpecificObject func, Requested inactive object");
                 throw new NotFoundException("Requested inactive object");
             }
             return this.converter.toBoundary(objectE.get());
     }
        this.logger.debug("in getSpecificObject func, %s".formatted(SUPERAPP_MINIAPP_USERS_ONLY_EXCEPTION));
        throw new ForbbidenOperationException(SUPERAPP_MINIAPP_USERS_ONLY_EXCEPTION);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SuperAppObjectBoundary> getChildren(String objectSuperapp, String internalObjectId,
                                                    String userSuperapp, String email,
                                                    int size, int page) {
        UserPK userId = new UserPK(userSuperapp, email);
        PageRequest pageReq = PageRequest.of(page, size, DEFAULT_SORTING_DIRECTION, "superapp", "objectId");

        SuperAppObjectEntity parent =
                this.objectRepository.findById(new SuperappObjectPK(objectSuperapp,internalObjectId))
                        .orElseThrow(()-> new NotFoundException("Cannot find requested object"));


        if (this.isValidUserCredentials(userId, SUPERAPP_USER, this.userRepository))
            return this.objectRepository
                    .findByParentsContaining(parent,pageReq)
                    .stream()
                    .map(this.converter::toBoundary)
                    .collect(Collectors.toList());

        if (this.isValidUserCredentials(userId, MINIAPP_USER, this.userRepository)) {
            return this.objectRepository
                    .findByParentsContainingAndActiveIsTrue(parent,pageReq)
                    .stream()
                    .map(this.converter::toBoundary)
                    .collect(Collectors.toList());
        }
        this.logger.debug("in getChildren func, %s".formatted(SUPERAPP_MINIAPP_USERS_ONLY_EXCEPTION));
        throw new ForbbidenOperationException(SUPERAPP_MINIAPP_USERS_ONLY_EXCEPTION);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SuperAppObjectBoundary> getParents(String objectSuperapp, String internalObjectId,
                                                   String userSuperapp, String email, int size, int page) {
        UserPK userId = new UserPK(userSuperapp, email);
        PageRequest pageReq = PageRequest.of(page, size, DEFAULT_SORTING_DIRECTION, "superapp", "objectId");

        SuperAppObjectEntity child =
                this.objectRepository.findById(new SuperappObjectPK(objectSuperapp,internalObjectId))
                        .orElseThrow(()-> new NotFoundException("Cannot find requested object"));

        if(this.isValidUserCredentials(userId, SUPERAPP_USER, this.userRepository))
            return this.objectRepository
                    .findByChildrenContaining(child,pageReq)
                    .stream()
                    .map(this.converter::toBoundary)
                    .collect(Collectors.toList());

        if(this.isValidUserCredentials(userId, MINIAPP_USER, this.userRepository))
            return this.objectRepository
                    .findByChildrenContainingAndActiveIsTrue(child,pageReq)
                    .stream()
                    .map(this.converter::toBoundary)
                    .collect(Collectors.toList());
        this.logger.debug("in getParents func, %s".formatted(SUPERAPP_MINIAPP_USERS_ONLY_EXCEPTION));
        throw new ForbbidenOperationException(SUPERAPP_MINIAPP_USERS_ONLY_EXCEPTION);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SuperAppObjectBoundary> getAllObjects(String userSuperapp, String email,
                                                      int size, int page) {
        UserPK userId = new UserPK(userSuperapp, email);
        PageRequest pageReq = PageRequest.of(page, size, DEFAULT_SORTING_DIRECTION, "superapp", "userEmail");

        if (this.isValidUserCredentials(userId, SUPERAPP_USER, this.userRepository))
            return this.objectRepository.findAll(pageReq)
                    .stream()
                    .map(this.converter::toBoundary)
                    .collect(Collectors.toList());

        if(this.isValidUserCredentials(userId, MINIAPP_USER, this.userRepository))
            return this.objectRepository.findAllByActiveIsTrue(pageReq)
                    .stream()
                    .map(this.converter::toBoundary)
                    .collect(Collectors.toList());
        this.logger.debug("in getAllObjects func, %s".formatted(SUPERAPP_MINIAPP_USERS_ONLY_EXCEPTION));
        throw new ForbbidenOperationException(SUPERAPP_MINIAPP_USERS_ONLY_EXCEPTION);
    }

    @Override
    public List<SuperAppObjectBoundary> searchObjectsByType(String type, String userSuperapp,
                                                            String email, int size, int page) {
        UserPK userId = new UserPK(userSuperapp, email);
        PageRequest pageReq = PageRequest.of(page, size, DEFAULT_SORTING_DIRECTION, "superapp", "objectId");

        if( isValidUserCredentials(userId, SUPERAPP_USER, this.userRepository))
            return this.objectRepository.findByType(type, pageReq)
                    .stream()
                    .map(this.converter::toBoundary)
                    .collect(Collectors.toList());

        if (isValidUserCredentials(userId, MINIAPP_USER, this.userRepository))
            return this.objectRepository.findByTypeAndActiveIsTrue(type, pageReq)
                    .stream()
                    .map(this.converter::toBoundary)
                    .collect(Collectors.toList());
        this.logger.debug("in SearchObjectByTYpe func, %s".formatted(SUPERAPP_MINIAPP_USERS_ONLY_EXCEPTION));
        throw new ForbbidenOperationException(SUPERAPP_MINIAPP_USERS_ONLY_EXCEPTION);
    }

    @Override
    @Transactional
    public List<SuperAppObjectBoundary> searchObjectsByExactAlias(String alias, String userSuperapp,
                                                                  String email, int size, int page) {
        UserPK userId = new UserPK(userSuperapp, email);
        PageRequest pageReq = PageRequest.of(page, size, DEFAULT_SORTING_DIRECTION, "superapp", "objectId");

        if (this.isValidUserCredentials(userId, SUPERAPP_USER, this.userRepository))
            return this.objectRepository.findByAlias(alias, pageReq)
                    .stream()
                    .map(this.converter::toBoundary)
                    .collect(Collectors.toList());

        if (this.isValidUserCredentials(userId, MINIAPP_USER, this.userRepository))
            return this.objectRepository.findByAliasAndActiveIsTrue(alias, pageReq)
                    .stream()
                    .map(this.converter::toBoundary)
                    .collect(Collectors.toList());
        this.logger.debug("in SearchObjectsByExactAlias func, %s".formatted(SUPERAPP_MINIAPP_USERS_ONLY_EXCEPTION));
        throw new ForbbidenOperationException(SUPERAPP_MINIAPP_USERS_ONLY_EXCEPTION);
    }

    @Override
    @Transactional
    public List<SuperAppObjectBoundary> searchObjectsByAliasContaining(String text, String userSuperapp,
                                                                       String email, int size, int page) {
        UserPK userId = new UserPK(userSuperapp, email);
        PageRequest pageReq = PageRequest.of(page, size, DEFAULT_SORTING_DIRECTION, "superapp", "objectId");

        if (this.isValidUserCredentials(userId, SUPERAPP_USER, this.userRepository))
            return this.objectRepository
                    .findByAliasContaining(text, pageReq)
                    .stream()
                    .map(this.converter::toBoundary)
                    .collect(Collectors.toList());

        if (this.isValidUserCredentials(userId, MINIAPP_USER, this.userRepository))
            return this.objectRepository
                    .findByActiveIsTrueAndAliasContaining(text, pageReq)
                    .stream()
                    .map(this.converter::toBoundary)
                    .collect(Collectors.toList());
        this.logger.debug("in SearchObjectsByAliasContaining func, %s".formatted(SUPERAPP_MINIAPP_USERS_ONLY_EXCEPTION));
        throw new ForbbidenOperationException(SUPERAPP_MINIAPP_USERS_ONLY_EXCEPTION);
    }

    public List<SuperAppObjectBoundary> getObjectsByCreationTimestamp(String creationEnum, String userSuperapp,
                                                                      String email, int size, int page) {

        UserPK userId = new UserPK(userSuperapp, email);
        if(!isValidTimeframes(creationEnum))
            throw new InvalidInputException("Invalid timeframe");

        if (!isValidUserCredentials(userId, SUPERAPP_USER, this.userRepository))
            throw new ForbbidenOperationException(SUPERAPP_USER_ONLY_EXCEPTION);

        Timeframes timeframe = Timeframes.valueOf(creationEnum);

        return this.objectRepository
                .findAllByCreationTimestampAfter(new Date(System.currentTimeMillis()-timeframe.getValue()*1000),
                        PageRequest.of(page, size, DEFAULT_SORTING_DIRECTION,
                                "creationTimestamp", "objectId"))
                .stream()
                .map(this.converter::toBoundary)
                .toList();
    }

    private void handleObject(SuperAppObjectBoundary object) {
        String objectType = object.getType();
        if (!isValidObjectType(objectType))
            objectType = "";

        switch (objectType) {
            case ("Transaction"), ("Group") -> {
                this.miniAppService = this.context.getBean("Split", SplitService.class);
            }
            case ("GrabPoll") -> {
                this.miniAppService = this.context.getBean("Grab", GrabService.class);
            }
            case ("Drive") -> {
                this.miniAppService = this.context.getBean("Lift", LiftService.class);
            }
            default -> {
                this.logger.debug("in handleObject func, Unknwon Object type");
                throw new InvalidInputException("Unknown object type");
            }
        }
        miniAppService.handleObjectByType(object);
    }

    private void handleObjectBinding(SuperAppObjectEntity parent, SuperAppObjectEntity child, UserPK userId) {
        if (child.getType().equals(Group.name()) && ObjectTypes.isValidObjectType(parent.getType()))
            throw new InvalidInputException("Cannot bind miniapp object as a parent");

        this.miniAppService = null; // this is done for the code to realize if the bounded objects has any limitations
        if (child.getType().equals(Transaction.name()) && parent.getType().equals(Group.name()))
            this.miniAppService = this.context.getBean("Split", SplitService.class);

        else if (child.getType().equals(GrabPoll.toString()))
            this.miniAppService = this.context.getBean("Grab", GrabService.class);

        else if (child.getType().equals(Drive.toString()))
            this.miniAppService = this.context.getBean("Lift", LiftService.class);

        if (this.miniAppService != null)
            this.miniAppService.checkValidBinding(parent, child, userId);
    }

    @Override
    @Transactional
    public void deleteAllObjects(String userSuperapp, String email) {
        UserPK userId = new UserPK(userSuperapp, email);
        if (isValidUserCredentials(userId, ADMIN, this.userRepository)) {
            this.objectRepository.deleteAll();
            this.logger.info("Deleted all super app objects successfully");
        }
        else {
            this.logger.debug("in deleteAllObjects func, %s".formatted(SUPERAPP_USER_ONLY_EXCEPTION));
            throw new ForbbidenOperationException(SUPERAPP_USER_ONLY_EXCEPTION);
        }
    }
}
