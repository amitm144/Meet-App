package superapp.logic.concreteServices;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import superapp.boundaries.command.MiniAppCommandBoundary;
import superapp.converters.MiniappCommandConverter;
import superapp.dal.IdGeneratorRepository;
import superapp.dal.MiniAppCommandRepository;
import superapp.dal.UserEntityRepository;
import superapp.data.IdGeneratorEntity;
import superapp.data.UserEntity;
import superapp.logic.AbstractService;
import superapp.logic.AdvancedMiniAppCommandsService;
import superapp.util.exceptions.InvalidInputException;
import superapp.util.EmailChecker;
import superapp.util.exceptions.NotFoundException;
import superapp.util.wrappers.SuperAppObjectIdWrapper;
import superapp.util.wrappers.UserIdWrapper;

import java.util.*;
import java.util.stream.Collectors;

import static superapp.data.UserRole.ADMIN;
import static superapp.util.ControllersConstants.DEFAULT_SORTING_DIRECTION;

@Service
public class MiniAppCommandService extends AbstractService implements AdvancedMiniAppCommandsService {
    private MiniappCommandConverter miniAppConverter;
    private MiniAppCommandRepository miniappRepository;
    private UserEntityRepository userRepository;
    private IdGeneratorRepository idGenerator;

    @Autowired
    public MiniAppCommandService(MiniappCommandConverter miniAppConverter,
                                 MiniAppCommandRepository miniappRepository,
                                 IdGeneratorRepository idGenerator,
                                 UserEntityRepository userRepository) {
        this.miniAppConverter = miniAppConverter;
        this.miniappRepository = miniappRepository;
        this.idGenerator = idGenerator;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public Object invokeCommand(MiniAppCommandBoundary command) {
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
        throw new NotFoundException("Method is Dperecated");
    }

    @Override
    @Deprecated
    @Transactional(readOnly = true)
    public List<MiniAppCommandBoundary> getAllCommands() {
        throw new NotFoundException("Method is Dperecated");
    }

    @Override
    @Deprecated
    @Transactional
    public void deleteAllCommands() {
        throw new NotFoundException("Method is Dperecated");
    }

    @Override
    @Transactional(readOnly = true)
    public List<MiniAppCommandBoundary> getAllCommands(String userSuperapp, String email,int size,int page) {
        UserEntity.UserPK userId = new UserEntity.UserPK(userSuperapp, email);
        this.isValidUserCredentials(userId, ADMIN, this.userRepository);

        return this.miniappRepository
                .findAll(PageRequest.of(page,size, DEFAULT_SORTING_DIRECTION,"miniapp","internalCommandId"))
                .stream()
                .map(this.miniAppConverter::toBoundary)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MiniAppCommandBoundary> getAllMiniAppCommands(String miniappName ,String userSuperapp, String email,int size,int page) {
        UserEntity.UserPK userId = new UserEntity.UserPK(userSuperapp, email);
        this.isValidUserCredentials(userId, ADMIN, this.userRepository);

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
        UserEntity.UserPK userId = new UserEntity.UserPK(userSuperapp, email);
        this.isValidUserCredentials(userId, ADMIN, this.userRepository);
        this.miniappRepository.deleteAll();
    }
}
