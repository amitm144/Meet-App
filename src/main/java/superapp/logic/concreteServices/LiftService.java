package superapp.logic.concreteServices;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import superapp.boundaries.command.MiniAppCommandBoundary;
import superapp.boundaries.lift.LiftRequestBoundary;
import superapp.boundaries.object.SuperAppObjectBoundary;
import superapp.boundaries.user.UserIdBoundary;
import superapp.converters.SuperAppObjectConverter;
import superapp.converters.UserConverter;
import superapp.dal.SuperAppObjectEntityRepository;
import superapp.data.ObjectTypes;
import superapp.data.SuperAppObjectEntity;
import superapp.data.SuperappObjectPK;
import superapp.data.UserPK;
import superapp.logic.LiftsService;
import superapp.logic.MiniAppServices;
import superapp.util.exceptions.CannotProcessException;
import superapp.util.exceptions.ForbbidenOperationException;
import superapp.util.exceptions.InvalidInputException;
import superapp.util.exceptions.NotFoundException;
import superapp.util.geoLocationAPI.DirectionsAPIHandler;
import superapp.util.geoLocationAPI.MapBoxConverter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static superapp.data.ObjectTypes.*;
import static superapp.util.Constants.*;

@Service("Lift")
public class LiftService implements LiftsService, MiniAppServices {
    private SuperAppObjectEntityRepository objectRepository;
    private SuperAppObjectConverter objectConverter;
    private UserConverter userConverter;
    private DirectionsAPIHandler directionsHandler;
    private Log logger = LogFactory.getLog(LiftService.class);

    private final String MISSING_VALUE_ERROR = "Drive %s must be specified";
    private final String DEFAULT_LANGUAGE = "EN";

    @Autowired
    public LiftService(SuperAppObjectEntityRepository objectRepository,
                       SuperAppObjectConverter objectConverter,
                       UserConverter userConverter) {
        this.objectRepository = objectRepository;
        this.objectConverter = objectConverter;
        this.userConverter = userConverter;
        this.directionsHandler = new DirectionsAPIHandler(new MapBoxConverter());
    }

    @Override
    public void handleObjectByType(SuperAppObjectBoundary object) {
        String objectType = object.getType();
        if (!isValidObjectType(objectType))
            objectType = "";

        if (objectType.equals(Drive.name())) {
            this.checkDriveData(object);
        } else {
            this.logger.error("in handleObjectByType func - %s".formatted(UNKNOWN_OBJECT_EXCEPTION));
            throw new InvalidInputException(UNKNOWN_OBJECT_EXCEPTION);
        }
    }

    @Override
    public Object runCommand(MiniAppCommandBoundary command) {
        SuperappObjectPK targetObjectKey = this.objectConverter.idToEntity(command.getTargetObject().getObjectId());
        UserIdBoundary invokedBy = command.getInvokedBy().getUserId();
        SuperAppObjectEntity drive = this.objectRepository.findById(targetObjectKey)
                .orElseThrow(() ->  {
                    this.logger.error("in runCommand func - %s"
                            .formatted(VALUE_NOT_FOUND_EXCEPTION.formatted("Drive")));
                    return new NotFoundException(VALUE_NOT_FOUND_EXCEPTION.formatted("Drive "));
                });
        SuperAppObjectEntity group =
                drive.getParents()
                        .stream()
                        .findFirst() // single drive can only be bound to one parent
                        .orElseThrow(() ->  {
                            this.logger.error("in runCommand func - %s"
                                    .formatted(OBJECT_NOT_BOUND_EXCEPTION.formatted("Drive")));
                            return new NotFoundException(OBJECT_NOT_BOUND_EXCEPTION.formatted("Drive"));
                        });

        if (!isUserInGroup(group, invokedBy)) {
            this.logger.error("in runCommand func - %s".formatted(USER_NOT_IN_GROUP_EXCEPTION));
            throw new InvalidInputException(USER_NOT_IN_GROUP_EXCEPTION);
        }
        if (!(group.getActive() && drive.getActive())) {
            this.logger.error("in runCommand func - %s".formatted(EXECUTE_ON_INACTIVE_EXCEPTION
                    .formatted("group or drive")));
            throw new InvalidInputException(EXECUTE_ON_INACTIVE_EXCEPTION.formatted("group or drive"));
        }
        String commandCase = command.getCommand();
        switch (commandCase) {
            case "StartDrive" -> {
                if (!drive.getCreatedBy().getUserId().equals(invokedBy)) {
                    this.logger.error("in runCommand func - Only the driver can start the drive");
                    throw new ForbbidenOperationException("Only the driver can start the drive");
                }
                return this.startDrive(drive);
            }
            case "LiftRequest" -> {
                this.checkRequestData(command);
                this.addNewRequest(command);
            }
            case "ApproveLiftRequest" -> {
                UserIdBoundary requestingUser = this.userConverter
                        .mapToBoundary((Map<String, String>)command.getCommandAttributes().get("userId"));
                this.approveLiftRequest(drive, requestingUser);
            }
            case "RejectLiftRequest" -> {
                UserIdBoundary requestingUser = this.userConverter
                        .mapToBoundary((Map<String, String>)command.getCommandAttributes().get("userId"));
                this.rejectLiftRequest(drive, requestingUser);
            }
            default -> {
                this.logger.error("in runCommand func - %s".formatted(UNKNOWN_COMMAND_EXCEPTION));
                throw new NotFoundException(UNKNOWN_COMMAND_EXCEPTION);}
        }
        return null;
    }

    @Override
    public void checkValidBinding(SuperAppObjectEntity parent, SuperAppObjectEntity child, UserPK userId) {
        if (!parent.getType().equals(ObjectTypes.Group.name())) {
            this.logger.error("in checkValidBinding func - Cannot bind drive to non-group objects");
            throw new InvalidInputException("Cannot bind drive to non-group objects");
        }
        if (child.getParents().size() > 0) {
            this.logger.error("in checkValidBinding func - Drive can only be bound to one group");
            throw new ForbbidenOperationException("Drive can only be bound to one group");
        }
        if (!isUserInGroup(parent, new UserIdBoundary(userId.getSuperapp(), userId.getEmail()))) {
            this.logger.error("in checkValidBinding func - Drives can only be bound by users in the group");
            throw new CannotProcessException("Drives can only be bound by users in the group");
        }
    }

    @Override
    public Object startDrive(SuperAppObjectEntity drive) {
        Map<String, Object> objectDetails = this.objectConverter.detailsToMap(drive.getObjectDetails());
        List<String> addresses = new ArrayList<>();
        // create address list from origin to destination
        addresses.add((String)objectDetails.get("origin"));
        this.mapListToBoundaryList((List<Map<String, Object>>)objectDetails.get("registeredPassengers"))
                .stream()
                .map(LiftRequestBoundary::getOrigin)
                .forEach(addresses::add);
        addresses.add((String)objectDetails.get("destination"));
        try {
            objectDetails.put("routeDetails", this.directionsHandler.getDirectionsByAddress(DEFAULT_LANGUAGE, addresses));
            drive.setActive(false);
            drive.setObjectDetails(this.objectConverter.detailsToString(objectDetails));
            this.objectRepository.save(drive);
            this.logger.info("Drive saved successfully");
            return this.objectConverter.toBoundary(drive);
        } catch (HttpClientErrorException e) {
            this.logger.error("in startDrive func - %s".formatted(e.getMessage()));
            throw new ForbbidenOperationException(e.getMessage());
        }
    }

    @Override
    public void approveLiftRequest(SuperAppObjectEntity drive, UserIdBoundary requestingUser) {
        if (!isUserRequestingPassenger(drive, requestingUser)) {
            this.logger.error("in approveLiftRequest func - User has never requested to join this lift");
            throw new InputMismatchException("User has never requested to join this lift");
        }
        Map<String, Object> objectDetails = this.objectConverter.detailsToMap(drive.getObjectDetails());
        List<LiftRequestBoundary> requestedList =
                this.mapListToBoundaryList((List<Map<String, Object>>)objectDetails.get("requestingPassengers"));
        List<LiftRequestBoundary> registeredList = this.mapListToBoundaryList(
                (List<Map<String, Object>>)objectDetails.getOrDefault("registeredPassengers", new ArrayList<>()));

        for (LiftRequestBoundary request : requestedList) {
            if (request.getUserId().equals(requestingUser)) {
                registeredList.add(request);
                requestedList.remove(request); // user can only register once to a drive
                break;
            }
        }
        objectDetails.replace("requestingPassengers", requestedList);
        if (objectDetails.containsKey("registeredPassengers"))
            objectDetails.replace("registeredPassengers", registeredList);
        else
            objectDetails.put("registeredPassengers", registeredList);
        drive.setObjectDetails(this.objectConverter.detailsToString(objectDetails));
        this.objectRepository.save(drive);
        this.logger.info("Approve Lift request success, Drive saved successfully");

    }

    @Override
    public void rejectLiftRequest(SuperAppObjectEntity drive, UserIdBoundary requestingUser) {
        if (!isUserRequestingPassenger(drive, requestingUser)) {
            this.logger.error("in rejectLiftRequest func - User has never requested to join this lift");
            throw new InputMismatchException("User has never requested to join this lift");
        }
        Map<String, Object> objectDetails = this.objectConverter.detailsToMap(drive.getObjectDetails());
        List<LiftRequestBoundary> requestedList =
                this.mapListToBoundaryList((List<Map<String, Object>>)objectDetails.get("requestingPassengers"));

        requestedList.removeIf(request -> request.getUserId().equals(requestingUser)); // user can only register once to a drive
        objectDetails.replace("requestingPassengers", requestedList);
        drive.setObjectDetails(this.objectConverter.detailsToString(objectDetails));
        this.objectRepository.save(drive);
        this.logger.info("Reject Lift Request, Drive saved successfully");
    }

    private void addNewRequest(MiniAppCommandBoundary request) {
        UserIdBoundary invokingUser = request.getInvokedBy().getUserId();
        SuperappObjectPK targetObject = this.objectConverter.idToEntity(request.getTargetObject().getObjectId());
        SuperAppObjectEntity requestedDrive = this.objectRepository.findById(targetObject)
                .orElseThrow(() -> {
                    this.logger.error("in addNewRequest func - %s".formatted(VALUE_NOT_FOUND_EXCEPTION
                            .formatted("Drive")));
                    return new NotFoundException(VALUE_NOT_FOUND_EXCEPTION.formatted("Drive"));
                });

        String key = "requestingPassengers";
        Map<String, Object> objectDetails = this.objectConverter.detailsToMap(requestedDrive.getObjectDetails());
        List<LiftRequestBoundary> requestList =
                (List<LiftRequestBoundary>)objectDetails.getOrDefault(key, new ArrayList<>());
        String origin = request.getCommandAttributes().get("origin").toString();

        requestList.add(new LiftRequestBoundary(invokingUser,origin));
        if (objectDetails.containsKey(key))
            objectDetails.replace(key, requestList);
        else
            objectDetails.put(key, requestList);
        requestedDrive.setObjectDetails(this.objectConverter.detailsToString(objectDetails));
        this.objectRepository.save(requestedDrive);
        this.logger.info("Lift - Add new Request succeeded, requested Drive saved successfully");

    }

    private boolean isUserInGroup(SuperAppObjectEntity group, UserIdBoundary userId) {
        List<UserIdBoundary> members = this.userConverter.mapListToBoundaryList(
                (List<Map<String, String>>)this.objectConverter
                        .detailsToMap(group.getObjectDetails())
                        .get("members"));

        return (members != null && members.contains(userId));
    }

    private boolean isUserRequestingPassenger(SuperAppObjectEntity drive, UserIdBoundary userId) {
        if (userId == null) {
            this.logger.error("in isUserRequestingPassenger - Missing user details");
            throw new InvalidInputException("Missing user details");
        }
        List<UserIdBoundary> requesting = mapListToBoundaryList(
                (List<Map<String, Object>>) this.objectConverter
                        .detailsToMap(drive.getObjectDetails())
                        .getOrDefault("requestingPassengers", new ArrayList<>()))
                .stream()
                .map(LiftRequestBoundary::getUserId)
                .toList();

        return requesting.contains(userId);
    }

    private boolean isUserRegisteredPassenger(SuperAppObjectEntity drive, UserIdBoundary userId) {
        if (userId == null) {
            this.logger.error("in isUserRegisteredPassenger - Missing user details");
            throw new InvalidInputException("Missing user details");
        }
        List<UserIdBoundary> registered = mapListToBoundaryList(
                (List<Map<String, Object>>) this.objectConverter
                        .detailsToMap(drive.getObjectDetails())
                        .getOrDefault("registeredPassengers", new ArrayList<>()))
                .stream()
                .map(LiftRequestBoundary::getUserId)
                .toList();

        return registered.contains(userId);
    }

    private void checkDriveData(SuperAppObjectBoundary drive) {
        Map<String, Object> details = drive.getObjectDetails();
        if (details == null) {
            this.logger.error("in checkDriveData - %s".formatted(MISSING_VALUE_ERROR.formatted("details")));
            throw new InvalidInputException(MISSING_VALUE_ERROR.formatted("details"));
        }
        String origin = (String) details.get("origin");
        String dest = (String) details.get("destination");
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
        Date time;
        try {
            time = ft.parse(details.get("time").toString());
        } catch (ParseException e) {
            this.logger.error("in checkDriveData - Invalid date");
            throw new InvalidInputException("Invalid date");
        }

        if (origin == null || origin.isBlank()) {
            this.logger.error("in checkDriveData - %s".formatted(MISSING_VALUE_ERROR.formatted("origin")));
            throw new InvalidInputException(MISSING_VALUE_ERROR.formatted("origin"));
        }
        if (dest == null || dest.isBlank()) {
            this.logger.error("in checkDriveData - %s".formatted(MISSING_VALUE_ERROR.formatted("destination")));
            throw new InvalidInputException(MISSING_VALUE_ERROR.formatted("destination"));
        }
        if (time == null || time.before(new Date()) || time.equals(new Date())) {
            this.logger.error("in checkDriveData - %s".formatted(MISSING_VALUE_ERROR.formatted("time")));
            throw new InvalidInputException(MISSING_VALUE_ERROR.formatted("time"));
        }
    }

    private void checkRequestData(MiniAppCommandBoundary request) {
        UserIdBoundary invokingUser = request.getInvokedBy().getUserId();
        SuperappObjectPK targetObject = this.objectConverter.idToEntity(request.getTargetObject().getObjectId());

        SuperAppObjectEntity requestedDrive = this.objectRepository.findById(targetObject)
                .orElseThrow(() -> new NotFoundException(VALUE_NOT_FOUND_EXCEPTION.formatted("Drive")));

        if (!requestedDrive.getType().equals(Drive.name())) {
            this.logger.error("in checkRequestData func - %s".formatted(WRONG_OBJECT_EXCEPTION));
            throw new InvalidInputException(WRONG_OBJECT_EXCEPTION);
        }
        if (!requestedDrive.getActive()) {
            this.logger.error("in checkRequestData func - %s".formatted(EXECUTE_ON_INACTIVE_EXCEPTION
                    .formatted("drive")));
            throw new InvalidInputException(EXECUTE_ON_INACTIVE_EXCEPTION.formatted("drive"));
        }
        if (isUserRegisteredPassenger(requestedDrive, invokingUser) || isUserRequestingPassenger(requestedDrive, invokingUser)) {
            this.logger.error("in checkRequestData func - Already registered or requested to join this lift");
            throw new ForbbidenOperationException("Already registered or requested to join this lift");
        }
        String origin = (String) request.getCommandAttributes().get("origin");
        if (origin == null) {
            this.logger.error("in checkRequestData func - %s".formatted(VALUE_NOT_FOUND_EXCEPTION
                    .formatted("Request origin")));
            throw new InvalidInputException(VALUE_NOT_FOUND_EXCEPTION.formatted("Request origin"));
        }
        this.logger.debug("Lift - Request Data checked successfully");
    }

    private List<LiftRequestBoundary> mapListToBoundaryList(List<Map<String, Object>> list) {
        if (list == null)
            return new ArrayList<>();

        List<LiftRequestBoundary> boundaryList = new ArrayList<>();
        for (Map<String, Object> map: list) {
            if (!(map.containsKey("userId") && map.containsKey("origin"))) {
                this.logger.error("in mapListToBoundaryList func - Missing or invalid data");
                throw new InvalidInputException("Missing or invalid data");
            }

            UserIdBoundary userId = this.userConverter.mapToBoundary((Map<String,String>)map.get("userId"));
            boundaryList.add(new LiftRequestBoundary(userId,map.get("origin").toString()));
        }
        return boundaryList;
    }
}
