package com.superapp.controllers;

import com.superapp.boundaries.object.ObjectIdBoundary;
import com.superapp.boundaries.object.ObjectBoundary;
import com.superapp.boundaries.user.UserIdBoundary;
import com.superapp.util.wrappers.UserIdWrapper;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.superapp.boundaries.object.ObjectBoundary.getNRandomObjects;

@RestController
public class ObjectsController {

    @RequestMapping(
            path = {"/superapp/objects"},
            method = {RequestMethod.POST},
            produces = {MediaType.APPLICATION_JSON_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE}
    )
    @ResponseBody
    public ObjectBoundary createObject(@RequestBody ObjectBoundary objectBoundary) {
        objectBoundary.setObjectId(new ObjectIdBoundary());
        objectBoundary.setCreationTimeStamp(new Date());
        return objectBoundary;
    }

    @RequestMapping(
            path = {"/superapp/objects/{superapp}/{InternalObjectId}"},
            method = {RequestMethod.PUT},
            consumes = {MediaType.APPLICATION_JSON_VALUE}
    )
    public void updateObject(ObjectBoundary objectBoundary,
                             @PathVariable String superapp,
                             @PathVariable String InternalObjectId) {
        //TODO - Update the specific object in DB with the superApp and InternalObjectId vars
    }

    @RequestMapping(
            path = {"/superapp/objects/{superapp}/{InternalObjectd}"},
            method = {RequestMethod.GET},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ObjectBoundary retrieveObject(@PathVariable String superapp, @PathVariable String InternalObjectd) {
        //TODO need to query from the DB one object from the superApp parameter and InternalObjectId parameter.
        Map<String, Object> tempMap = new HashMap<>();
        tempMap.put("key", "temp");
        tempMap.put("key2", "temp2");
        // For Example, I created an object to show some data.
        return new ObjectBoundary((new ObjectIdBoundary(InternalObjectd)),
                "example-type", "a", tempMap,
                new UserIdWrapper(new UserIdBoundary("dvir.tayeb@gmail.com"))
        );
    }

    @RequestMapping(
            path = {"/superapp/objects"},
            method = {RequestMethod.GET},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @ResponseBody
    public ArrayList<ObjectBoundary> getAllObjects() {
        //TODO need to query from the DB to get all objects we want.

        // For example, we returned some random array data.
        return getNRandomObjects(3);
    }
}
