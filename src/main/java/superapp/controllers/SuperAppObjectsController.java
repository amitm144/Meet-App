package superapp.controllers;

import superapp.boundaries.object.SuperAppObjectBoundary;
import superapp.boundaries.object.SuperAppObjectIdBoundary;
import superapp.logic.AdvancedSuperAppObjectsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
public class SuperAppObjectsController {

    private AdvancedSuperAppObjectsService objService;
    private final String DEFAULT_PAGE_SIZE = "10";
    private final String DEFAULT_PAGE = "0";

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
                             @PathVariable String InternalObjectId,
                             @RequestParam(name = "userSuperapp", required = true,defaultValue = "") String userSuperapp,
                             @RequestParam(name = "userEmail", required = true,defaultValue = "") String email) {
        this.objService.updateObject(superapp, InternalObjectId, objectBoundary,userSuperapp,email);
    }

    @RequestMapping(
            path = {"/superapp/objects/{superapp}/{InternalObjectId}"},
            method = {RequestMethod.GET},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public SuperAppObjectBoundary retrieveObject(@PathVariable String superapp,
                                                 @PathVariable String InternalObjectId,
                                                 @RequestParam(name="userSuperapp", required = true, defaultValue = "") String userSupperapp,
                                                 @RequestParam(name="userEmail", required = true, defaultValue = "") String email) {
        return this.objService.getSpecificObject(superapp,InternalObjectId,userSupperapp,email);
    }

    @RequestMapping(
            path = {"/superapp/objects"},
            method = {RequestMethod.GET},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public SuperAppObjectBoundary[] getAllObjects(
            @RequestParam(name="userSuperapp", required = true, defaultValue = "") String userSupperapp,
            @RequestParam(name="userEmail", required = true, defaultValue = "") String email,
            @RequestParam(name = "size", required = false, defaultValue = DEFAULT_PAGE_SIZE) int size,
            @RequestParam(name="page", required = false, defaultValue = "0") int page) {
        return this.objService.getAllObjects(userSupperapp,email,size,page).toArray(new SuperAppObjectBoundary[0]);
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
                                                   @RequestParam(name="userSuperapp", required = true, defaultValue = "") String userSuperapp,
                                                   @RequestParam(name="userEmail", required = true, defaultValue = "") String email,
                                                   @RequestParam(name = "size", required = false, defaultValue = DEFAULT_PAGE_SIZE) int size,
                                                   @RequestParam(name="page", required = false, defaultValue = DEFAULT_PAGE) int page) {
        return this.objService.getChildren(superapp, internalObjectId,userSuperapp,email,size,page).toArray(new SuperAppObjectBoundary[0]);
    }

    @RequestMapping(
            path="/superapp/objects/{superapp}/{internalObjectId}/parents",
            method = {RequestMethod.GET},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public SuperAppObjectBoundary[] getAllParents(@PathVariable String superapp,
                                                  @PathVariable String internalObjectId,
                                                  @RequestParam(name="userSuperapp", required = true, defaultValue = "") String userSuperapp,
                                                  @RequestParam(name="userEmail", required = true, defaultValue = "") String email,
                                                  @RequestParam(name = "size", required = false, defaultValue = DEFAULT_PAGE_SIZE) int size,
                                                  @RequestParam(name="page", required = false, defaultValue = DEFAULT_PAGE) int page) {
        return this.objService.getParents(superapp, internalObjectId,userSuperapp,email,size,page).toArray(new SuperAppObjectBoundary[0]);
    }

    @RequestMapping(
            path="/superapp/objects//superapp/objects/search/byType/{type}",
            method = {RequestMethod.GET},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public SuperAppObjectBoundary[] SearchObjectsByType( @PathVariable String type,
                                                         @RequestParam(name= "userSuperapp", required = true, defaultValue = "") String userSuperapp,
                                                         @RequestParam(name="userEmail", required = true, defaultValue = "") String email,
                                                         @RequestParam(name = "size", required = false, defaultValue = DEFAULT_PAGE_SIZE) int size,
                                                         @RequestParam(name="page", required = false, defaultValue = DEFAULT_PAGE) int page) {
        return this.objService.SearchObjectsByType(type, userSuperapp, email, size, page).toArray(new SuperAppObjectBoundary[0]);
    }

    @RequestMapping(
            path="/superapp/objects/search/byAlias/{alias}",
            method = {RequestMethod.GET},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public SuperAppObjectBoundary[] SearchObjectsByExactAlias(@PathVariable String alias,
                                                              @RequestParam(name="userSuperapp", required = true, defaultValue = "") String userSuperapp,
                                                              @RequestParam(name="userEmail", required = true, defaultValue = "") String email,
                                                              @RequestParam(name = "size", required = false, defaultValue = DEFAULT_PAGE_SIZE) int size,
                                                              @RequestParam(name="page", required = false, defaultValue = DEFAULT_PAGE) int page) {
        return this.objService.SearchObjectsByExactAlias(alias, userSuperapp, email, size, page).toArray(new SuperAppObjectBoundary[0]);
    }

    @RequestMapping(
            path="/superapp//superapp/objects/search/byAliasContaining/{text}",
            method = {RequestMethod.GET},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public SuperAppObjectBoundary[] SearchObjectsByExactAliasContainingText(@PathVariable String text,
                                                              @RequestParam(name="userSuperapp", required = true, defaultValue = "") String userSuperapp,
                                                              @RequestParam(name="userEmail", required = true, defaultValue = "") String email,
                                                              @RequestParam(name = "size", required = false, defaultValue = DEFAULT_PAGE_SIZE) int size,
                                                              @RequestParam(name="page", required = false, defaultValue = DEFAULT_PAGE) int page) {
        return this.objService.SearchObjectsByExactAliasContainingText(text, userSuperapp, email, size, page).toArray(new SuperAppObjectBoundary[0]);
    }

}
