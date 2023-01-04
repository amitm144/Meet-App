package superapp.logic.concreteServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import superapp.boundaries.object.SuperAppObjectBoundary;
import superapp.boundaries.user.UserIdBoundary;
import superapp.data.SuperAppObjectEntity;
import superapp.util.exceptions.InvalidInputException;
import superapp.util.wrappers.SuperAppObjectIdWrapper;

import java.util.Map;

@Service
public class ServiceHandler implements ServicesFactory {
    private SplitService splitService;
    private GrabService grabService;

    //private GrabService grabService
    //private LiftService liftService
    @Autowired
    public ServiceHandler(SplitService splitService//GrabService grabService , LiftService liftService

    ) {
        this.splitService = splitService;
        //grabService = grabService;
        //liftService =liftService;
    }

    @Override
    public void handleObjectByType(SuperAppObjectBoundary object) {
        switch (object.getType()) {
            case ("Transaction"): // Only Object in Miniap that need to be modifyed
            {
                this.splitService.handleObjectByType(object);
                break;
            }
        }
    }

    @Override
    public void updateObjectDetails(SuperAppObjectEntity object) {
        switch (object.getType()) {
            case ("Transaction"):// Only Object in Miniap that need to be modifyed
            {
                splitService.updateObjectDetails(object);
                break;
            }
        }
    }

    @Override
    public Object runCommand(String miniapp,
                             SuperAppObjectIdWrapper targetObject,
                             UserIdBoundary invokedBy,
                             String commandCase, Map<String, Object> commandAttributes) {
        switch (miniapp) {
            case ("Split"): {
                return this.splitService.runCommand(miniapp, targetObject, invokedBy, commandCase, commandAttributes);
            }
            case ("Grab"): {
                this.grabService.runCommand(miniapp, targetObject, invokedBy, commandCase, commandAttributes);
                break;
            }
            case ("Lift"): {
                //this.liftService.runCommand(miniapp,targetObject,user,attributes,commandCase);
                break;
            }
            default: {
                throw new InvalidInputException("MiniApp Not Found");
            }
        }
        return null;
    }
}