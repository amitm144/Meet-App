package superapp.controllers;


import superapp.boundaries.object.ObjectBoundary;
import superapp.boundaries.object.ObjectIdBoundary;
import superapp.logic.ObjectsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import superapp.util.SuperappChecker;

import java.util.List;

@RestController
public class ObjectsController {

    private ObjectsService objService;
    private final SuperappChecker checker = new SuperappChecker();

    @Autowired
    public void setObjectService(ObjectsService objService) {
        this.objService = objService;
    }

    @RequestMapping(
            path = {"/superapp/objects"},
            method = {RequestMethod.POST},
            produces = {MediaType.APPLICATION_JSON_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE}
    )
    @ResponseBody
    public ObjectBoundary createObject(@RequestBody ObjectBoundary objectBoundary) {
        ObjectIdBoundary id = objectBoundary.getObjectId();
        if (id != null && !checker.isValidSuperapp(id.getSuperapp()))
            throw new RuntimeException("Incorrect superapp");
        return this.objService.createObject(objectBoundary);
    }

    @RequestMapping(
            path = {"/superapp/objects/{superapp}/{InternalObjectId}"},
            method = {RequestMethod.PUT},
            consumes = {MediaType.APPLICATION_JSON_VALUE}
    )
    public void updateObject(@RequestBody ObjectBoundary objectBoundary,
                             @PathVariable String superapp,
                             @PathVariable String InternalObjectId) {
        ObjectIdBoundary id = objectBoundary.getObjectId();
        if (id != null && !checker.isValidSuperapp(id.getSuperapp()))
            throw new RuntimeException("Incorrect superapp");
        this.objService.updateObject(superapp, InternalObjectId, objectBoundary);
    }

    @RequestMapping(
            path = {"/superapp/objects/{superapp}/{InternalObjectId}"},
            method = {RequestMethod.GET},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ObjectBoundary retrieveObject(@PathVariable String superapp,
                                         @PathVariable String InternalObjectId) {
        if (!checker.isValidSuperapp(superapp))
            throw new RuntimeException("Incorrect superapp");
        return this.objService.getSpecificObject(superapp,InternalObjectId);
    }

    @RequestMapping(
            path = {"/superapp/objects"},
            method = {RequestMethod.GET},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @ResponseBody
    public ObjectBoundary[] getAllObjects() {
        List<ObjectBoundary> l = this.objService.getAllObjects();
        return l.toArray(new ObjectBoundary[0]);
    }
}
