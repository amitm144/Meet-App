package superapp.logic.concreteServices;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import superapp.boundaries.command.MiniAppCommandBoundary;
import superapp.converters.MiniappCommandConverter;
import superapp.dal.IdGeneratorRepository;
import superapp.dal.MiniAppCommandRepository;
import superapp.data.IdGeneratorEntity;
import superapp.data.MiniAppCommandEntity;
import superapp.logic.AbstractService;
import superapp.logic.MiniAppCommandsService;
import superapp.util.EmailChecker;
import superapp.util.wrappers.ObjectIdWrapper;
import superapp.util.wrappers.UserIdWrapper;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class MiniAppCommandService extends AbstractService implements MiniAppCommandsService {
    private MiniappCommandConverter miniAppConverter;
    private MiniAppCommandRepository miniappRepository;
    private IdGeneratorRepository idGenerator;

    @Autowired
    public MiniAppCommandService(MiniappCommandConverter miniAppConverter,
                                 MiniAppCommandRepository miniappRepository,
                                 IdGeneratorRepository idGenerator) {
        this.miniAppConverter = miniAppConverter;
        this.miniappRepository = miniappRepository;
        this.idGenerator = idGenerator;
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
            throw new RuntimeException("Invoked by fields cannot be missing or empty");

        if (!EmailChecker.isValidEmail(invokedBy.getUserId().getEmail()))
            throw new RuntimeException("Invalid invoking user email");

        ObjectIdWrapper targetObject = command.getTargetObject();
        if (targetObject == null ||
                targetObject.getObjectId() == null ||
                targetObject.getObjectId().getSuperapp() == null ||
                targetObject.getObjectId().getInternalObjectId() == null ||
                targetObject.getObjectId().getSuperapp().isBlank() ||
                targetObject.getObjectId().getInternalObjectId().isBlank())
            throw new RuntimeException("Target object fields cannot be missing or empty");

        if (command.getCommand() == null || command.getCommand().isEmpty())
            throw new RuntimeException("Command attribute cannot be missing or empty");

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
    public List<MiniAppCommandBoundary> getALlCommands() {
        return StreamSupport
                .stream(this.miniappRepository.findAll().spliterator(), false)
                .map(this.miniAppConverter::toBoundary)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MiniAppCommandBoundary> getAllMiniAppCommands(String miniappName) {
        Iterable<MiniAppCommandEntity> miniappCommands = this.miniappRepository.findAllByMiniapp(miniappName);
        return StreamSupport
                .stream(miniappCommands.spliterator(), false)
                .map(this.miniAppConverter::toBoundary)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteALlCommands() { this.miniappRepository.deleteAll(); }
}
