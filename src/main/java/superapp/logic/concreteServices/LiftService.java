package superapp.logic.concreteServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import superapp.boundaries.command.MiniAppCommandBoundary;
import superapp.boundaries.object.SuperAppObjectBoundary;
import superapp.boundaries.user.UserIdBoundary;
import superapp.converters.SuperAppObjectConverter;
import superapp.dal.SuperAppObjectEntityRepository;
import superapp.data.ObjectTypes;
import superapp.data.SuperAppObjectEntity;
import superapp.data.SuperappObjectPK;
import superapp.data.UserPK;
import superapp.logic.LiftsService;
import superapp.logic.MiniAppServices;
import superapp.util.exceptions.ForbbidenOperationException;
import superapp.util.exceptions.InvalidInputException;
import superapp.util.exceptions.NotFoundException;

import java.util.*;

import static superapp.data.ObjectTypes.*;
import static superapp.util.Constants.*;

@Service("Lift")
public class LiftService implements LiftsService, MiniAppServices {
    private SuperAppObjectEntityRepository objectRepository;
    private SuperAppObjectConverter converter;

    private final String MISSING_VALUE_ERROR = "Drive %s must be specified";

    @Autowired
    public LiftService(SuperAppObjectEntityRepository objectRepository) {
        this.objectRepository = objectRepository;
        this.converter = new SuperAppObjectConverter();
    }

    @Override
    public void handleObjectByType(SuperAppObjectBoundary object) {
        String objectType = object.getType();
        if (!isValidObjectType(objectType))
            objectType = "";

        if (objectType.equals(Drive.name())) {
            this.checkDriveData(object);
        } else {
            throw new InvalidInputException(UNKNOWN_OBJECT_EXCEPTION);
        }
    }

    @Override
    public Object runCommand(MiniAppCommandBoundary command) {
        SuperappObjectPK targetObjectKey = this.converter.idToEntity(command.getTargetObject().getObjectId());
        UserIdBoundary invokedBy = command.getInvokedBy().getUserId();
        SuperAppObjectEntity drive = this.objectRepository.findById(targetObjectKey)
                .orElseThrow(() ->  new NotFoundException(VALUE_NOT_FOUND_EXCEPTION.formatted("Drive ")));
        SuperAppObjectEntity group =
                drive.getParents()
                        .stream()
                        .findFirst() // single drive can only be bound to one parent
                        .orElseThrow(() ->  new NotFoundException(OBJECT_NOT_BOUND_EXCEPTION.formatted("Drive")));

        if (!isUserInGroup(group, invokedBy))
            throw new InvalidInputException(USER_NOT_IN_GROUP_EXCEPTION);
        if (!(group.getActive() && drive.getActive()))
            throw new InvalidInputException(EXECUTE_ON_INACTIVE_EXCEPTION.formatted("group or drive"));

        String commandCase = command.getCommand();
        switch (commandCase) {
            case "StartDrive" -> { return null; }
            case "LiftRequest" -> {
                checkRequestData(command);
                this.addNewRequest(command);
                return null;
            }
            case "ApproveLiftRequest" -> { return null; }
            case "RejectLiftRequests" -> { return null; }
            default -> throw new NotFoundException(UNKNOWN_COMMAND_EXCEPTION);
        }
    }

    @Override
    public void checkValidBinding(SuperAppObjectEntity parent, SuperAppObjectEntity child, UserPK userId) {
        if (!parent.getType().equals(ObjectTypes.Group.name()))
            throw new InvalidInputException("Cannot bind drive to non-group objects");
        if (child.getParents().size() > 0)
            throw new ForbbidenOperationException("Drive can only be bound to one group");
    }

    @Override
    public Object startDrive(SuperappObjectPK drive) {
        return null;
    }

    @Override
    public void approveLiftRequest(SuperappObjectPK drive, UserIdBoundary requestingUser) {

    }

    @Override
    public void rejectLiftRequest(SuperappObjectPK drive, UserIdBoundary requestingUser) {

    }

    private void addNewRequest(MiniAppCommandBoundary request) {
        UserIdBoundary invokingUser = request.getInvokedBy().getUserId();
        SuperappObjectPK targetObject = this.converter.idToEntity(request.getTargetObject().getObjectId());
        SuperAppObjectEntity requestedDrive = this.objectRepository.findById(targetObject)
                .orElseThrow(() -> new NotFoundException(VALUE_NOT_FOUND_EXCEPTION.formatted("Drive")));

        String key = "requestingPassengers";
        Map<String, Object> objectDetails = this.converter.detailsToMap(requestedDrive.getObjectDetails());
        List<UserIdBoundary> requestList = (List<UserIdBoundary>)objectDetails.getOrDefault(key, new ArrayList<>());
        requestList.add(invokingUser);
        objectDetails.replace(key, requestList);
        requestedDrive.setObjectDetails(this.converter.detailsToString(objectDetails));
        this.objectRepository.save(requestedDrive);
    }

    private boolean isUserInGroup(SuperAppObjectEntity group, UserIdBoundary userId) {
        LinkedHashMap<String, String> linkedMap = new LinkedHashMap<>();
        linkedMap.put("superapp", userId.getSuperapp());
        linkedMap.put("email", userId.getEmail());

        List<LinkedHashMap<String, String>> members = (List<LinkedHashMap<String, String>>)this.converter
                .detailsToMap(group.getObjectDetails())
                .get("members");

        return (members != null && members.contains(linkedMap));
    }

    private boolean isUserRequestingPassenger(SuperAppObjectEntity drive, UserIdBoundary userId) {
        List<UserIdBoundary> requesting =
                (List<UserIdBoundary>) this.converter.detailsToMap(drive.getObjectDetails())
                        .get("requestingPassengers");

        return requesting.contains(userId);
    }

    private boolean isUserRegisteredPassenger(SuperAppObjectEntity drive, UserIdBoundary userId) {
        List<UserIdBoundary> registered =
                (List<UserIdBoundary>) this.converter.detailsToMap(drive.getObjectDetails())
                        .get("registeredPassengers");

        return registered.contains(userId);
    }

    private void checkDriveData(SuperAppObjectBoundary drive) {
        Map<String, Object> details = (Map<String, Object>)drive.getObjectDetails().get("details");
        if (details == null)
            throw new InvalidInputException(MISSING_VALUE_ERROR.formatted("details"));

        String origin = (String) details.get("origin");
        String dest = (String) details.get("destination");
        Date time = (Date) details.get("time");

        if (origin == null || origin.isBlank())
            throw new InvalidInputException(MISSING_VALUE_ERROR.formatted("origin"));
        if (dest == null || dest.isBlank())
            throw new InvalidInputException(MISSING_VALUE_ERROR.formatted("destination"));
        if (time == null || time.before(new Date()) || time.equals(new Date()))
            throw new InvalidInputException(MISSING_VALUE_ERROR.formatted("time"));
    }

    private void checkRequestData(MiniAppCommandBoundary request) {
        UserIdBoundary invokingUser = request.getInvokedBy().getUserId();
        SuperappObjectPK targetObject = this.converter.idToEntity(request.getTargetObject().getObjectId());

        SuperAppObjectEntity requestedDrive = this.objectRepository.findById(targetObject)
                .orElseThrow(() -> new NotFoundException(VALUE_NOT_FOUND_EXCEPTION.formatted("Drive")));

        if (!requestedDrive.getType().equals(Drive.name()))
            throw new InvalidInputException(WRONG_OBJECT_EXCEPTION);
        if (!requestedDrive.getActive())
            throw new InvalidInputException(EXECUTE_ON_INACTIVE_EXCEPTION.formatted("drive"));
        if (isUserRegisteredPassenger(requestedDrive, invokingUser) ||isUserRequestingPassenger(requestedDrive, invokingUser))
            throw new ForbbidenOperationException("Already registered or requested to join this lift");
        String origin = (String) request.getCommandAttributes().get("origin");
        if (origin == null)
            throw new InvalidInputException(VALUE_NOT_FOUND_EXCEPTION.formatted("Request origin"));
    }
}
