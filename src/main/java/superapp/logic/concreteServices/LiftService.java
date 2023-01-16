package superapp.logic.concreteServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import superapp.boundaries.command.MiniAppCommandBoundary;
import superapp.boundaries.object.SuperAppObjectBoundary;
import superapp.boundaries.user.UserIdBoundary;
import superapp.converters.SuperAppObjectConverter;
import superapp.dal.SuperAppObjectEntityRepository;
import superapp.data.SuperAppObjectEntity;
import superapp.data.SuperappObjectPK;
import superapp.data.UserPK;
import superapp.logic.LiftsService;
import superapp.logic.MiniAppServices;
import superapp.util.exceptions.InvalidInputException;
import superapp.util.exceptions.NotFoundException;

import java.util.*;

import static superapp.data.ObjectTypes.*;

@Service("Lift")
public class LiftService implements LiftsService, MiniAppServices {
    private SuperAppObjectEntityRepository objectRepository;
    private SuperAppObjectConverter converter;

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
        switch (objectType) {
            case ("Drive") -> { return; }
            default -> throw new InvalidInputException("Unknown object type");
        }
    }

    @Override
    public Object runCommand(MiniAppCommandBoundary command) {
//        SuperAppObjectEntity group =
//                this.objectRepository.findById(
//                                new SuperappObjectPK(
//                                        targetObject.getObjectId().getSuperapp(),
//                                        targetObject.getObjectId().getInternalObjectId()))
//                        .orElseThrow(() ->  new NotFoundException("Group not found"));
//
//        if (!isUserInGroup(group, invokedBy))
//            throw new InvalidInputException("Invoking user is not part of this group");
//
//        if (!group.getActive())
//            throw new InvalidInputException("Cannot execute commands on an inactive group");

        String commandCase = command.getCommand();
        switch (commandCase) {
            case "StartDrive" -> { return null; }
            case "ApproveLiftRequest" -> { return null; }
            case "RejectLiftRequests" -> { return null; }
            default -> throw new NotFoundException("Unknown command");
        }
    }

    @Override
    public void checkValidBinding(SuperAppObjectEntity parent, SuperAppObjectEntity child, UserPK userId) {

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

    private boolean isUserRequestingPassenger(SuperAppObjectEntity drive, UserIdBoundary userId) {
        LinkedHashMap<String, String> linkedMap = new LinkedHashMap<>();
        linkedMap.put("superapp", userId.getSuperapp());
        linkedMap.put("email", userId.getEmail());

        return ((List<LinkedHashMap<String, String>>)this.converter
                .detailsToMap(drive.getObjectDetails()).get("members"))
                .contains(linkedMap);
    }

    private boolean isUserRegisteredPassenger(SuperAppObjectEntity drive, UserIdBoundary userId) {
        LinkedHashMap<String, String> linkedMap = new LinkedHashMap<>();
        linkedMap.put("superapp", userId.getSuperapp());
        linkedMap.put("email", userId.getEmail());

        return ((List<LinkedHashMap<String, String>>)this.converter
                .detailsToMap(drive.getObjectDetails()).get("members"))
                .contains(linkedMap);
    }

    private void checkDriveData(SuperAppObjectBoundary group) {

    }

    private void checkRequestData(SuperAppObjectBoundary transaction) {

    }
}
