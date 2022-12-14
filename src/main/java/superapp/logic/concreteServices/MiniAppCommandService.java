package superapp.logic.concreteServices;

import superapp.boundaries.command.MiniAppCommandBoundary;
import superapp.boundaries.object.ObjectIdBoundary;
import superapp.boundaries.user.UserIdBoundary;
import superapp.converters.MiniappCommandConverter;
import superapp.data.MiniAppCommandEntity;
import superapp.logic.AbstractService;
import superapp.logic.MiniAppCommandsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MiniAppCommandService extends AbstractService implements MiniAppCommandsService {

    private MiniappCommandConverter miniAppConverter;
    private Map<String, ArrayList<MiniAppCommandEntity>> miniAppsCommands; // { miniapp: List of miniAppCommands }

    @Autowired
    public MiniAppCommandService(MiniappCommandConverter miniAppConverter) {
        this.miniAppConverter = miniAppConverter;
    }

    @PostConstruct
    public void setup() {
        this.miniAppsCommands = Collections.synchronizedMap(new HashMap<>());
    }

    @Override
    public Object invokeCommand(MiniAppCommandBoundary command) {
        // TODO: add check for known miniapp
        // issue internalCommandId, tie with superapp and set invocation timestamp
        command.setInvocationTimestamp(new Date());
        command.getCommandId().setInternalCommandId(Integer.toString(this.miniAppsCommands.size() + 1));
        command.getCommandId().setSuperapp(getSuperappName());

        UserIdBoundary invokedBy = command.getInvokedBy().getUserId();
        ObjectIdBoundary targetObject = command.getTargetObject().getObjectId();
        String miniapp = command.getCommandId().getMiniapp();
        if (invokedBy != null && !isValidSuperapp(invokedBy.getSuperapp()) ||
                targetObject != null && !isValidSuperapp(targetObject.getSuperapp())) {
            throw new RuntimeException("Incorrect superapp");
        }

        if (miniAppsCommands.get(miniapp) == null) { // create new entry if there is no log for relevant miniapp
            ArrayList<MiniAppCommandEntity> commandList = new ArrayList<MiniAppCommandEntity>();
            miniAppsCommands.put(miniapp,commandList);
        }
        miniAppsCommands.get(miniapp).add(this.miniAppConverter.toEntity(command));
        return command;
    }

    @Override
    public List<MiniAppCommandBoundary> getALlCommands() {
        return this.miniAppsCommands
                .values()
                .stream()
                .flatMap(Collection::stream) // breaks every ArrayList to its individual values and returns all of them as stream
                .map(this.miniAppConverter::toBoundary)
                .collect(Collectors.toList());
    }

    @Override
    public List<MiniAppCommandBoundary> getAllMiniAppCommands(String miniAppName) {
        ArrayList<MiniAppCommandEntity> commands = this.miniAppsCommands.get(miniAppName);
        if (commands == null)
            return new ArrayList<MiniAppCommandBoundary>();

        return commands
                .stream()
                .map(this.miniAppConverter::toBoundary)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteALlCommands() {
        this.miniAppsCommands.clear();
    }
}
