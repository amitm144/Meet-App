package superapp.logic.concreteServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import superapp.boundaries.command.MiniAppCommandBoundary;
import superapp.boundaries.object.SuperAppObjectBoundary;
import superapp.boundaries.user.UserIdBoundary;
import superapp.logic.MiniAppServiceHandler;
import superapp.util.exceptions.InvalidInputException;
import superapp.util.wrappers.SuperAppObjectIdWrapper;

import static superapp.data.ObjectTypes.isValidObjectType;

@Service
public class ServiceHandler implements MiniAppServiceHandler {
    private SplitService splitService;
    private GrabService grabService;
    //private LiftService liftService

    @Autowired
    public ServiceHandler(SplitService splitService ,GrabService grabService /*, LiftService liftService*/) {
        this.splitService = splitService;
        this.grabService = grabService;
//        this.liftService =liftService;
    }

    public void handleObjectByType(SuperAppObjectBoundary object) {
        String objectType = object.getType();
        if (!isValidObjectType(objectType))
            objectType = "";
        switch (objectType) {
            case ("TRANSACTION"), ("GROUP") -> this.splitService.handleObjectByType(object);
        }
    }

    @Override
    public Object runCommand(MiniAppCommandBoundary command) {
        String miniapp = command.getCommandId().getMiniapp();
        switch (miniapp) {
            case ("Split") -> { return this.splitService.runCommand(command); }
            case ("Grab") -> {
                return this.grabService.runCommand(command);
            }
            case ("Lift") -> { return null; }
            default -> { throw new InvalidInputException("Unknown miniapp"); }
        }
    }
}