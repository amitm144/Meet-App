package superapp.logic.concreteServices;

import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import superapp.boundaries.command.MiniAppCommandBoundary;
import superapp.boundaries.command.MiniAppCommandIdBoundary;
import superapp.boundaries.object.SuperAppObjectBoundary;
import superapp.converters.MiniappCommandConverter;
import superapp.converters.SuperAppObjectConverter;
import superapp.dal.IdGeneratorRepository;
import superapp.dal.MiniAppCommandRepository;
import superapp.dal.SuperAppObjectEntityRepository;
import superapp.dal.UserEntityRepository;
import superapp.data.*;
import superapp.data.IdGeneratorEntity;
import superapp.data.UserPK;
import superapp.logic.AbstractService;
import superapp.logic.AdvancedMiniAppCommandsService;
import superapp.util.exceptions.ForbbidenOperationException;
import superapp.util.exceptions.InvalidInputException;
import superapp.util.EmailChecker;
import superapp.util.exceptions.NotFoundException;
import superapp.util.wrappers.SuperAppObjectIdWrapper;
import superapp.util.wrappers.UserIdWrapper;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static superapp.data.UserRole.ADMIN;
import static superapp.util.Constants.*;

@Service
public class MiniAppCommandService extends AbstractService implements AdvancedMiniAppCommandsService {
    private MiniappCommandConverter miniAppConverter;
    private SuperAppObjectConverter superAppObjectConverter;
    private MiniAppCommandRepository miniappRepository;
    private IdGeneratorRepository idGenerator;
    private SuperAppObjectEntityRepository objectRepository;
    private UserEntityRepository userEntityRepository;

    private SuperAppObjectEntityRepository superAppObjectEntityRepository;

    @Autowired
    public MiniAppCommandService(MiniappCommandConverter miniAppConverter,
                                 SuperAppObjectConverter superAppObjectConverter,
                                 MiniAppCommandRepository miniappRepository,
                                 IdGeneratorRepository idGenerator,
                                 UserEntityRepository userRepository,
                                 SuperAppObjectEntityRepository superAppObjectEntityRepository,
                                 SuperAppObjectEntityRepository objectRepository) {
        this.miniAppConverter = miniAppConverter;
        this.superAppObjectConverter = superAppObjectConverter;
        this.miniappRepository = miniappRepository;
        this.superAppObjectEntityRepository = superAppObjectEntityRepository;
        this.idGenerator = idGenerator;
        this.userEntityRepository = userRepository;
        this.objectRepository =objectRepository;
    }

    @Override
    @Transactional
    public Object invokeCommand(MiniAppCommandBoundary command) {
        checkInvokedCommand(command); // will throw an exception if invalid command

        // issue internalCommandId, tie with superapp and set invocation timestamp
        IdGeneratorEntity helper = this.idGenerator.save(new IdGeneratorEntity());
        String commandId = helper.getId().toString();
        this.idGenerator.delete(helper);
        command.getCommandId().setInternalCommandId(commandId);
        command.setInvocationTimestamp(new Date());
        command.getCommandId().setSuperapp(this.superappName);
        this.miniappRepository.save(this.miniAppConverter.toEntity(command));
        /*
            TODO:
             add check for known miniapp
             if known - point to miniapp service
             otherwise throw error (command is already been saved)
        */
        return command;
    }

    @Override
    @Transactional(readOnly = true)
    @Deprecated
    public List<MiniAppCommandBoundary> getAllMiniAppCommands(String miniappName) {
        throw new NotFoundException(DEPRECATED_EXCEPTION);
    }

    @Override
    @Deprecated
    @Transactional(readOnly = true)
    public List<MiniAppCommandBoundary> getAllCommands() {
        throw new NotFoundException(DEPRECATED_EXCEPTION);
    }

    @Override
    @Deprecated
    @Transactional
    public void deleteAllCommands() {
        throw new NotFoundException(DEPRECATED_EXCEPTION);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MiniAppCommandBoundary> getAllCommands(String userSuperapp, String email,int size,int page) {
        UserPK userId = new UserPK(userSuperapp, email);
        if(!isValidUserCredentials(userId, ADMIN, this.userEntityRepository))
            throw new ForbbidenOperationException(ADMIN_ONLY_EXCEPTION);

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
        if(!isValidUserCredentials(userId, ADMIN, this.userEntityRepository))
            throw new ForbbidenOperationException(ADMIN_ONLY_EXCEPTION);

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
        if(!isValidUserCredentials(userId, ADMIN, this.userEntityRepository))
            throw new ForbbidenOperationException(ADMIN_ONLY_EXCEPTION);
        this.miniappRepository.deleteAll();
    }

    private void checkInvokedCommand(MiniAppCommandBoundary command){
        UserIdWrapper invokedBy = command.getInvokedBy();
        if (invokedBy == null ||
                invokedBy.getUserId() == null ||
                invokedBy.getUserId().getSuperapp() == null ||
                invokedBy.getUserId().getEmail() == null ||
                invokedBy.getUserId().getSuperapp().isBlank() ||
                invokedBy.getUserId().getEmail().isBlank())
            throw new InvalidInputException("Invoked by fields cannot be missing or empty");

        if (!EmailChecker.isValidEmail(invokedBy.getUserId().getEmail()))
            throw new InvalidInputException("Invalid invoking user email");

        SuperAppObjectIdWrapper targetObject = command.getTargetObject();
        if (targetObject == null ||
                targetObject.getObjectId() == null ||
                targetObject.getObjectId().getSuperapp() == null ||
                targetObject.getObjectId().getInternalObjectId() == null ||
                targetObject.getObjectId().getSuperapp().isBlank() ||
                targetObject.getObjectId().getInternalObjectId().isBlank())
            throw new InvalidInputException("Target object fields cannot be missing or empty");

        if (command.getCommand() == null || command.getCommand().isEmpty())
            throw new InvalidInputException("Command attribute cannot be missing or empty");

        // issue internalCommandId, tie with superapp and set invocation timestamp
        Optional<SuperAppObjectEntity> objectE =
                this.objectRepository.findById(new SuperappObjectPK(
                        targetObject.getObjectId().getSuperapp(),
                        targetObject.getObjectId().getInternalObjectId()));

        if(objectE.isEmpty())
            throw new NotFoundException("Object Not Found");

        if(!isValidUserCredentials(new UserPK(invokedBy.getUserId().getSuperapp(), invokedBy.getUserId().getEmail()),
                UserRole.MINIAPP_USER,this.userEntityRepository))
            throw new ForbbidenOperationException(MINIAPP_USER_ONLY_EXCEPTION);

        if(!objectE.get().getActive())
            throw new ForbbidenOperationException("Cannot preform actions on an inactive object");
    }

    @Override
    @Transactional
    public SuperAppObjectBoundary updateObjectCreationTimestamp(String userSuperapp,
                                                                String userEmail,
                                                                MiniAppCommandBoundary objectTimeTravel) {
        // Validate Admin user:
        UserPK userId = new UserPK(userSuperapp, userEmail);
        if(!isValidUserCredentials(userId, ADMIN, this.userEntityRepository))
            throw new ForbbidenOperationException(ADMIN_ONLY_EXCEPTION);
        // Validate correct command:
        if(!objectTimeTravel.getCommand().equals("objectTimeTravel")){
            throw new RuntimeException("Can't create Object Timestamp");
        }
        // Find object in db and update:
        String internalObjectId = objectTimeTravel.getTargetObject().getObjectId().getInternalObjectId();
        Optional<SuperAppObjectEntity> objectE = this.superAppObjectEntityRepository.findById(
                new SuperappObjectPK(userSuperapp,internalObjectId));
        if (objectE.isEmpty())
            throw new NotFoundException("Unknown object");
        SuperAppObjectBoundary updatedObjectTimeTravel = this.superAppObjectConverter.toBoundary(objectE.get());
        try {
            String d = objectTimeTravel.getCommandAttributes().get("creationTimestamp").toString();
            SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
            updatedObjectTimeTravel.setCreationTimestamp(ft.parse(d));
        }catch (Exception e) {
            System.out.println("Can't update creation timestamp - invalid Date");
            return null;
        }
        return updatedObjectTimeTravel;
    }

    @Override
    @Transactional
    public MiniAppCommandBoundary storeMiniAppCommand(String userSuperapp,
                                                      String userEmail,
                                                      MiniAppCommandBoundary miniappCommandBoundary) {
        // Validate Admin user:
        UserPK userId = new UserPK(userSuperapp, userEmail);
        if(!isValidUserCredentials(userId, ADMIN, this.userEntityRepository))
            throw new ForbbidenOperationException(ADMIN_ONLY_EXCEPTION);
        // Validate correct command:
        if(!miniappCommandBoundary.getCommand().equals("echo")){
            throw new RuntimeException("Can't store Mini-App Command");
        }

        UserIdWrapper invokedBy = miniappCommandBoundary.getInvokedBy();
        if (invokedBy == null ||
                invokedBy.getUserId() == null ||
                invokedBy.getUserId().getSuperapp() == null ||
                invokedBy.getUserId().getEmail() == null ||
                invokedBy.getUserId().getSuperapp().isBlank() ||
                invokedBy.getUserId().getEmail().isBlank())
            throw new InvalidInputException("Invoked by fields cannot be missing or empty");

        if (!EmailChecker.isValidEmail(invokedBy.getUserId().getEmail()))
            throw new InvalidInputException("Invalid invoking user email");

        SuperAppObjectIdWrapper targetObject = miniappCommandBoundary.getTargetObject();
        if (targetObject == null ||
                targetObject.getObjectId() == null ||
                targetObject.getObjectId().getSuperapp() == null ||
                targetObject.getObjectId().getInternalObjectId() == null ||
                targetObject.getObjectId().getSuperapp().isBlank() ||
                targetObject.getObjectId().getInternalObjectId().isBlank())
            throw new InvalidInputException("Target object fields cannot be missing or empty");

        if (miniappCommandBoundary.getCommand() == null || miniappCommandBoundary.getCommand().isEmpty())
            throw new InvalidInputException("Command attribute cannot be missing or empty");

        IdGeneratorEntity helper = this.idGenerator.save(new IdGeneratorEntity());
        String commandId = helper.getId().toString();
        this.idGenerator.delete(helper);
        MiniAppCommandIdBoundary miniAppCommandIdBoundary = new MiniAppCommandIdBoundary("TEST", commandId);
        miniAppCommandIdBoundary.setSuperapp(this.superappName);
        miniappCommandBoundary.setCommandId(miniAppCommandIdBoundary);
        miniappCommandBoundary.setInvocationTimestamp(new Date());
        return miniappCommandBoundary;
    }
}
