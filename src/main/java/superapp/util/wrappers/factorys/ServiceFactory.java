package superapp.util.wrappers.factorys;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import superapp.boundaries.object.SuperAppObjectBoundary;
import superapp.data.SuperAppObjectEntity;
import superapp.logic.ServicesFactory;
import superapp.logic.concreteServices.SplitService;
import superapp.util.wrappers.SuperAppObjectIdWrapper;
import superapp.util.wrappers.UserIdWrapper;
import java.util.Map;
@Service
public class ServiceFactory implements ServicesFactory {
    private SplitService splitService;
    //private GrabService grabService
    //private LiftService liftService
    @Autowired
    public ServiceFactory(SplitService splitService//GrabService grabService , LiftService liftService

    ) {
        this.splitService = splitService;
        //grabService = grabService;
        //liftService =liftService;
    }

    @Override
    public SuperAppObjectBoundary setObjectDetails(SuperAppObjectBoundary object) {
            switch (object.getType()){
                case ("Transaction"): // Only Object in Miniap that need to be modifyed
                    return this.splitService.setObjectDetails(object);
        }
        return null;
    }

    @Override
    public SuperAppObjectEntity updateObjectDetails(SuperAppObjectEntity object) {
        switch (object.getType()){
            case ("Transaction"):// Only Object in Miniap that need to be modifyed
                return this.splitService.updateObjectDetails(object);
        }
        return null;
    }

    @Override
    public void runCommand(String miniapp, SuperAppObjectIdWrapper targetObject, UserIdWrapper user, Map<String, Object> attributes, String commandCase) {
        switch (miniapp) {
            case("Split"):{
                    this.splitService.runCommand(miniapp,targetObject,user,attributes,commandCase);
                }
            case ("Grab"):{
                //this.grabService.runCommand(miniapp,targetObject,user,attributes,commandCase);
                return;
            }
            case("Lift"):{
                    //this.liftService.runCommand(miniapp,targetObject,user,attributes,commandCase);
                return;
            }
            default:{
                throw new RuntimeException("Miniapp Not Found");
            }
        }
        }
    }