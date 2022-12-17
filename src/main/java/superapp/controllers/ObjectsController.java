package superapp.controllers;

import superapp.boundaries.object.ObjectBoundary;
import superapp.boundaries.object.ObjectIdBoundary;
import superapp.logic.ObjectsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
public class ObjectsController {

    private ObjectsService objService;

    @Autowired
    public void setObjectService(ObjectsService objService) {
        this.objService = objService;
    }

    @RequestMapping(
            path = {"/superapp/objects"},
            method = {RequestMethod.POST},
            produces = {MediaType.APPLICATION_JSON_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public ObjectBoundary createObject(@RequestBody ObjectBoundary objectBoundary) {
        return this.objService.createObject(objectBoundary);
    }

    @RequestMapping(
            path = {"/superapp/objects/{superapp}/{InternalObjectId}"},
            method = {RequestMethod.PUT},
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    public void updateObject(@RequestBody ObjectBoundary objectBoundary,
                             @PathVariable String superapp,
                             @PathVariable String InternalObjectId) {
        this.objService.updateObject(superapp, InternalObjectId, objectBoundary);
    }

    @RequestMapping(
            path = {"/superapp/objects/{superapp}/{InternalObjectId}"},
            method = {RequestMethod.GET},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ObjectBoundary retrieveObject(@PathVariable String superapp,
                                         @PathVariable String InternalObjectId) {
        return this.objService.getSpecificObject(superapp,InternalObjectId);
    }

    @RequestMapping(
            path = {"/superapp/objects"},
            method = {RequestMethod.GET},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public ObjectBoundary[] getAllObjects() {
        return this.objService.getAllObjects().toArray(new ObjectBoundary[0]);
    }

    @RequestMapping(
            path="/superapp/objects/{superapp}/{internalObjectId}/children",
            method = {RequestMethod.PUT},
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    public void bindExistingObjects(@RequestBody ObjectIdBoundary toBind,
                                    @PathVariable String superapp,
                                    @PathVariable String internalObjectId) {}

    @RequestMapping(
            path="/superapp/objects/{superapp}/{internalObjectId}/children",
            method = {RequestMethod.GET},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public void getAllChildren(@PathVariable String superapp, @PathVariable String internalObjectId) {}

    @RequestMapping(
            path="/superapp/objects/{superapp}/{internalObjectId}/parents",
            method = {RequestMethod.GET},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public void getAllParents(@PathVariable String superapp, @PathVariable String internalObjectId) {}
}
