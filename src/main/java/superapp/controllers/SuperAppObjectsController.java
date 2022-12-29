package superapp.controllers;

import superapp.boundaries.object.SuperAppObjectBoundary;
import superapp.boundaries.object.SuperAppObjectIdBoundary;
import superapp.logic.AdvancedSuperAppObjectsService;
import superapp.logic.SuperAppObjectsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
public class SuperAppObjectsController {

    private AdvancedSuperAppObjectsService objService;

    @Autowired
    public void setObjectService(AdvancedSuperAppObjectsService objService) {
        this.objService = objService;
    }

    @RequestMapping(
            path = {"/superapp/objects"},
            method = {RequestMethod.POST},
            produces = {MediaType.APPLICATION_JSON_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public SuperAppObjectBoundary createObject(@RequestBody SuperAppObjectBoundary objectBoundary) {
        return this.objService.createObject(objectBoundary);
    }

    @RequestMapping(
            path = {"/superapp/objects/{superapp}/{InternalObjectId}"},
            method = {RequestMethod.PUT},
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    public void updateObject(@RequestBody SuperAppObjectBoundary objectBoundary,
                             @PathVariable String superapp,
                             @PathVariable String InternalObjectId) {
        this.objService.updateObject(superapp, InternalObjectId, objectBoundary);
    }

    @RequestMapping(
            path = {"/superapp/objects/{superapp}/{InternalObjectId}"},
            method = {RequestMethod.GET},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public SuperAppObjectBoundary retrieveObject(@PathVariable String superapp,
                                                 @PathVariable String InternalObjectId,
                                                 @RequestParam(name="userSupperapp", required = false, defaultValue = "") String userSupperapp,
                                                 @RequestParam(name="userEmail", required = false, defaultValue = "") String email) {
        return this.objService.getSpecificObject(superapp,InternalObjectId);
    }

    @RequestMapping(
            path = {"/superapp/objects"},
            method = {RequestMethod.GET},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public SuperAppObjectBoundary[] getAllObjects(
            @RequestParam(name="userSupperapp", required = false, defaultValue = "") String userSupperapp,
            @RequestParam(name="userEmail", required = false, defaultValue = "") String email,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size,
            @RequestParam(name="page", required = false, defaultValue = "0") int page) {
        return this.objService.getAllObjects(userSupperapp,email,size,page).toArray(new SuperAppObjectBoundary[0]);
        //return this.objService.getAllObjects().toArray(new SuperAppObjectBoundary[0]);
    }

    @RequestMapping(
            path="/superapp/objects/{superapp}/{internalObjectId}/children",
            method = {RequestMethod.PUT},
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    public void bindExistingObjects(@RequestBody SuperAppObjectIdBoundary toBind,
                                    @PathVariable String superapp,
                                    @PathVariable String internalObjectId) {
        this.objService.bindNewChild(superapp, internalObjectId, toBind);
    }

    @RequestMapping(
            path="/superapp/objects/{superapp}/{internalObjectId}/children",
            method = {RequestMethod.GET},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public SuperAppObjectBoundary[] getAllChildren(@PathVariable String superapp,
                                                   @PathVariable String internalObjectId,
                                                   @RequestParam(name = "size", required = false, defaultValue = "10") int size,
                                                   @RequestParam(name="page", required = false, defaultValue = "0") int page) {
        return this.objService.getChildren(superapp, internalObjectId).toArray(new SuperAppObjectBoundary[0]);
    }

    @RequestMapping(
            path="/superapp/objects/{superapp}/{internalObjectId}/parents",
            method = {RequestMethod.GET},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public SuperAppObjectBoundary[] getAllParents(@PathVariable String superapp,
                                                  @PathVariable String internalObjectId) {
        return this.objService.getParents(superapp, internalObjectId).toArray(new SuperAppObjectBoundary[0]);
    }

    @RequestMapping(
            path="/superapp/objects//superapp/objects/search/byType/{type}",
            method = {RequestMethod.GET},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public SuperAppObjectBoundary[] SearchObjectsByType( @PathVariable String type,
                                                         @RequestParam(name="userSupperapp", required = false, defaultValue = "") String userSupperapp,
                                                         @RequestParam(name="userEmail", required = false, defaultValue = "") String email,
                                                         @RequestParam(name = "size", required = false, defaultValue = "10") int size,
                                                         @RequestParam(name="page", required = false, defaultValue = "0") int page) {
        return this.objService.SearchObjectsByType(type,userSupperapp, email, size, page).toArray(new SuperAppObjectBoundary[0]);
    }

    @RequestMapping(
            path="/superapp/objects/search/byAlias/{alias}",
            method = {RequestMethod.GET},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public SuperAppObjectBoundary[] SearchObjectsByExactAlias(@PathVariable String alias,
                                                              @RequestParam(name="userSupperapp", required = false, defaultValue = "") String userSupperapp,
                                                              @RequestParam(name="userEmail", required = false, defaultValue = "") String email,
                                                              @RequestParam(name = "size", required = false, defaultValue = "10") int size,
                                                              @RequestParam(name="page", required = false, defaultValue = "0") int page) {
        return this.objService.SearchObjectsByExactAlias(alias, userSupperapp, email, size, page).toArray(new SuperAppObjectBoundary[0]);
    }

}
