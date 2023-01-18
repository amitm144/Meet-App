package superapp.logic.concreteServices;

import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import superapp.boundaries.command.MiniAppCommandBoundary;
import superapp.boundaries.command.MiniAppCommandIdBoundary;
import superapp.boundaries.object.SuperAppObjectBoundary;
import superapp.boundaries.user.UserBoundary;
import superapp.boundaries.user.UserIdBoundary;
import superapp.converters.MiniappCommandConverter;
import superapp.converters.SuperAppObjectConverter;
import superapp.converters.UserConverter;
import superapp.dal.IdGeneratorRepository;
import superapp.dal.MiniAppCommandRepository;
import superapp.dal.SuperAppObjectEntityRepository;
import superapp.dal.UserEntityRepository;
import superapp.data.*;
import superapp.data.IdGeneratorEntity;
import superapp.data.UserPK;
import superapp.logic.AbstractService;
import superapp.logic.AdvancedMiniAppCommandsService;
import superapp.logic.MiniAppServices;
import superapp.util.exceptions.ForbbidenOperationException;
import superapp.util.exceptions.InvalidInputException;
import superapp.util.EmailChecker;
import superapp.util.exceptions.NotFoundException;
import superapp.util.wrappers.SuperAppObjectIdWrapper;
import superapp.util.wrappers.UserIdWrapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static superapp.data.UserRole.ADMIN;
import static superapp.util.Constants.*;

@Service
public class MiniAppCommandService extends AbstractService implements AdvancedMiniAppCommandsService {
    private ApplicationContext context;
    private MiniAppServices miniAppService;
    private MiniappCommandConverter miniAppConverter;
    private SuperAppObjectConverter superAppObjectConverter;
    private IdGeneratorRepository idGenerator;
    private MiniAppCommandRepository miniappRepository;
    private SuperAppObjectEntityRepository objectRepository;
    private UserEntityRepository userEntityRepository;
    private Log logger = LogFactory.getLog(MiniAppCommandService.class);
    private UserConverter userConverter;

    @Autowired
    public MiniAppCommandService(MiniappCommandConverter miniAppConverter, ApplicationContext context,
                                 SuperAppObjectConverter superAppObjectConverter,IdGeneratorRepository idGenerator,
                                 MiniAppCommandRepository miniappRepository, UserEntityRepository userRepository,
                                 SuperAppObjectEntityRepository objectRepository) {
        this.miniAppConverter = miniAppConverter;
        this.superAppObjectConverter = superAppObjectConverter;
        this.miniappRepository = miniappRepository;
        this.idGenerator = idGenerator;
        this.userEntityRepository = userRepository;
        this.objectRepository =objectRepository;
        this.context = context;
    }

    @Override
    @Transactional
    public Object invokeCommand(MiniAppCommandBoundary command) {
        this.checkInvokedCommand(command, UserRole.MINIAPP_USER); // will throw an exception if invalid command

        // issue internalCommandId, tie with superapp and set invocation timestamp
        IdGeneratorEntity helper = this.idGenerator.save(new IdGeneratorEntity());
        String commandId = helper.getId().toString();
        this.idGenerator.delete(helper);
        command.getCommandId().setInternalCommandId(commandId);
        command.setInvocationTimestamp(new Date());
        command.getCommandId().setSuperapp(this.superappName);

        this.miniappRepository.save(this.miniAppConverter.toEntity(command));
        this.logger.info("saved Command successfully in db");
        return this.handleCommand(command);
    }

    @Override
    @Transactional(readOnly = true)
    @Deprecated
    public List<MiniAppCommandBoundary> getAllMiniAppCommands(String miniappName) {
        this.logger.debug("in getAllMiniAppCommands func, - %s".formatted(DEPRECATED_EXCEPTION));
        throw new NotFoundException(DEPRECATED_EXCEPTION);
    }

    @Override
    @Deprecated
    @Transactional(readOnly = true)
    public List<MiniAppCommandBoundary> getAllCommands() {
        this.logger.debug("in getAllCommands func,without scope vars - %s".formatted(DEPRECATED_EXCEPTION));
        throw new NotFoundException(DEPRECATED_EXCEPTION);
    }

    @Override
    @Deprecated
    @Transactional
    public void deleteAllCommands() {
        this.logger.debug("in deleteAllCommands func, without scope vars - %s".formatted(DEPRECATED_EXCEPTION));
        throw new NotFoundException(DEPRECATED_EXCEPTION);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MiniAppCommandBoundary> getAllCommands(String userSuperapp, String email,int size,int page) {
        UserPK userId = new UserPK(userSuperapp, email);
        if(!isValidUserCredentials(userId, ADMIN, this.userEntityRepository)) {
            this.logger.debug("in getAllCommands func - %s".formatted(ADMIN_ONLY_EXCEPTION));
            throw new ForbbidenOperationException(ADMIN_ONLY_EXCEPTION);
        }

        return this.miniappRepository
                .findAll(PageRequest.of(page,size, DEFAULT_SORTING_DIRECTION,"miniapp", "internalCommandId"))
                .stream()
                .map(this.miniAppConverter::toBoundary)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MiniAppCommandBoundary> getAllMiniAppCommands(String miniappName ,String userSuperapp, String email,int size,int page) {
        UserPK userId = new UserPK(userSuperapp, email);
        if(!isValidUserCredentials(userId, ADMIN, this.userEntityRepository)) {
            this.logger.debug("in getAllMiniAppCommands func - %s".formatted(ADMIN_ONLY_EXCEPTION));
            throw new ForbbidenOperationException(ADMIN_ONLY_EXCEPTION);
        }

        return this.miniappRepository.findAllByMiniapp(miniappName,
                        PageRequest.of(page,size, DEFAULT_SORTING_DIRECTION,"miniapp","internalCommandId"))
                .stream()
                .map(this.miniAppConverter::toBoundary)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteAllCommands(String userSuperapp, String email)
    {
        UserPK userId = new UserPK(userSuperapp, email);
        if(!isValidUserCredentials(userId, ADMIN, this.userEntityRepository)) {
            this.logger.debug("in deleteAllCommands func - %s".formatted(ADMIN_ONLY_EXCEPTION));
            throw new ForbbidenOperationException(ADMIN_ONLY_EXCEPTION);
        }
        this.logger.debug("Deleted All Commands successfully");
        this.miniappRepository.deleteAll();
    }

    @Override
    @Transactional
    public SuperAppObjectBoundary updateObjectCreationTimestamp(MiniAppCommandBoundary objectTimeTravel) {
        // Validate correct command:
        if(!objectTimeTravel.getCommand().equals("objectTimeTravel")) {
            this.logger.debug("in updateObjectCreationTimestamp func - Missing new CreationTimeStamp");
            throw new InvalidInputException("Missing new CreationTimestamp");
        }
        checkInvokedCommand(objectTimeTravel, ADMIN);
        // Find object in db and update:
        String internalObjectId = objectTimeTravel.getTargetObject().getObjectId().getInternalObjectId();
        UserIdBoundary userIdBoundary = objectTimeTravel.getInvokedBy().getUserId();
        Optional<SuperAppObjectEntity> objectE = this.objectRepository.findById(
                new SuperappObjectPK(userIdBoundary.getSuperapp(),internalObjectId));
        if (objectE.isEmpty()) {
            this.logger.debug("in updateObjectCreationTimestamp func - Unknown Object");
            throw new NotFoundException("Unknown object");
        }

        SuperAppObjectBoundary updatedObjectTimeTravel = this.superAppObjectConverter.toBoundary(objectE.get());
        if(!updatedObjectTimeTravel.getActive()) {
            this.logger.debug("in updateObjectCreationTimestamp func - Cannot execute time travel on inactive object");
            throw new ForbbidenOperationException("Cannot execute time travel on inactive object");
        }
        try {
            String d = objectTimeTravel.getCommandAttributes().get("creationTimestamp").toString();
            SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
            updatedObjectTimeTravel.setCreationTimestamp(ft.parse(d));
        } catch (Exception e) {
            this.logger.error("in updateObjectCreationTimestamp func - Invalid Date");
            throw new InvalidInputException("Can't update creation timestamp - invalid Date");
        }
        this.objectRepository.save(this.superAppObjectConverter.toEntity(updatedObjectTimeTravel));
        this.logger.debug("updated object timestamp successfully");
        return updatedObjectTimeTravel;
    }

    @Override
    @Transactional
    public MiniAppCommandBoundary storeMiniAppCommand(MiniAppCommandBoundary miniappCommandBoundary) {
        // Validate correct command:
        if (!miniappCommandBoundary.getCommand().equals("echo")) {
            this.logger.error("in storeMiniAppCommand func - Can't store MiniAppCommand");
            throw new RuntimeException("Can't store MiniAppCommand");
        }
        // Validate invoking user is admin and valid command boundary:
        checkInvokedCommand(miniappCommandBoundary, ADMIN);
        // store as new command
        IdGeneratorEntity helper = this.idGenerator.save(new IdGeneratorEntity());
        String commandId = helper.getId().toString();
        this.idGenerator.delete(helper);

        MiniAppCommandIdBoundary miniAppCommandIdBoundary = new MiniAppCommandIdBoundary("TEST", commandId);
        miniAppCommandIdBoundary.setSuperapp(this.superappName);
        miniappCommandBoundary.setCommandId(miniAppCommandIdBoundary);
        miniappCommandBoundary.setInvocationTimestamp(new Date());
        this.miniappRepository.save(this.miniAppConverter.toEntity(miniappCommandBoundary));
        this.logger.debug("stored miniAppCommand successfully");
        return miniappCommandBoundary;
    }

    private Object handleCommand(MiniAppCommandBoundary command) {
        String miniapp = command.getCommandId().getMiniapp();
        switch (miniapp) {
            case ("Split") -> {
                this.miniAppService = this.context.getBean("Split", SplitService.class);
            }
            case ("Grab") -> {
                this.miniAppService = this.context.getBean("Grab", GrabService.class);
            }
            case ("Lift") -> {
                this.miniAppService = this.context.getBean("Lift", LiftService.class);
            }
            default -> {
                this.logger.error("in handleCommand func - Unknown miniapp");
                throw new InvalidInputException("Unknown miniapp");
            }

        }
        return this.miniAppService.runCommand(command);
    }

    private void checkInvokedCommand(MiniAppCommandBoundary command,UserRole userRole){
        UserIdWrapper invokedBy = command.getInvokedBy();
        if (invokedBy == null ||
                invokedBy.getUserId() == null ||
                invokedBy.getUserId().getSuperapp() == null ||
                invokedBy.getUserId().getEmail() == null ||
                invokedBy.getUserId().getSuperapp().isBlank() ||
                invokedBy.getUserId().getEmail().isBlank()) {
            this.logger.debug("in checkInvokedCommand func - Invoked by fields cannot be missing or empty");
            throw new InvalidInputException("Invoked by fields cannot be missing or empty");
        }

        if (!EmailChecker.isValidEmail(invokedBy.getUserId().getEmail())) {
            this.logger.debug("in checkInvokedCommand func - Invalid invoking user email");
            throw new InvalidInputException("Invalid invoking user email");
        }

        SuperAppObjectIdWrapper targetObject = command.getTargetObject();
        if (targetObject == null ||
                targetObject.getObjectId() == null ||
                targetObject.getObjectId().getSuperapp() == null ||
                targetObject.getObjectId().getInternalObjectId() == null ||
                targetObject.getObjectId().getSuperapp().isBlank() ||
                targetObject.getObjectId().getInternalObjectId().isBlank()) {
            this.logger.debug("in checkInvokedCommand func - Target object fields cannot be missing or empty");
            throw new InvalidInputException("Target object fields cannot be missing or empty");
        }

        if (command.getCommand() == null || command.getCommand().isEmpty()) {
            this.logger.debug("in checkInvokedCommand func - Command attribute cannot be missing or empty");
            throw new InvalidInputException("Command attribute cannot be missing or empty");
        }

        // issue internalCommandId, tie with superapp and set invocation timestamp
        Optional<SuperAppObjectEntity> objectE =
                this.objectRepository.findById(new SuperappObjectPK(
                        targetObject.getObjectId().getSuperapp(),
                        targetObject.getObjectId().getInternalObjectId()));

        if(objectE.isEmpty()) {
            this.logger.error("in checkInvokedCommand func - Object Not Found");
            throw new NotFoundException("Object Not Found");
        }

        if(!isValidUserCredentials(new UserPK(invokedBy.getUserId().getSuperapp(), invokedBy.getUserId().getEmail()),
                userRole,this.userEntityRepository)) {
            this.logger.error("in checkInvokedCommand func - Operation allowed for %s only".formatted(userRole));
            throw new ForbbidenOperationException("Operation allowed for %s only".formatted(userRole));
        }

        if(!objectE.get().getActive()) {
            this.logger.error("in checkInvokedCommand func - Cannot preform actions on an inactive object");
            throw new ForbbidenOperationException("Cannot preform actions on an inactive object");
        }
    }
}
