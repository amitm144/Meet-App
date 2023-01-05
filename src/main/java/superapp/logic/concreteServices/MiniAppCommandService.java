package superapp.logic.concreteServices;

import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import superapp.boundaries.command.MiniAppCommandBoundary;
import superapp.boundaries.user.UserIdBoundary;
import superapp.converters.MiniappCommandConverter;
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

import java.util.*;
import java.util.stream.Collectors;

import static superapp.data.UserRole.ADMIN;
import static superapp.util.Constants.*;

@Service
public class MiniAppCommandService extends AbstractService implements AdvancedMiniAppCommandsService {
    private MiniappCommandConverter miniAppConverter;
    private MiniAppCommandRepository miniappRepository;
    private IdGeneratorRepository idGenerator;
    private SuperAppObjectEntityRepository objectRepository;
    private UserEntityRepository userEntityRepository;
    private ServiceHandler serviceHandler;

    @Autowired
    public MiniAppCommandService(MiniappCommandConverter miniAppConverter,
                                 MiniAppCommandRepository miniappRepository,
                                 IdGeneratorRepository idGenerator, ServiceHandler service,
                                 UserEntityRepository userRepository,
                                 SuperAppObjectEntityRepository objectRepository) {
        this.miniAppConverter = miniAppConverter;
        this.miniappRepository = miniappRepository;
        this.idGenerator = idGenerator;
        this.userEntityRepository = userRepository;
        this.objectRepository =objectRepository;
        this.serviceHandler = service;
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

        SuperAppObjectIdWrapper targetObject = command.getTargetObject();
        UserIdBoundary invokedBy = command.getInvokedBy().getUserId();
        // run command will handle any unknown miniapp by 400 - Bad request.
        return this.serviceHandler.runCommand(command);
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
}
