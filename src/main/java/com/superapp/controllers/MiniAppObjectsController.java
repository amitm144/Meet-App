package com.superapp.controllers;

import com.superapp.boundaries.command.CommandBoundary;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import java.util.Date;

@RestController
public class MiniAppObjectsController {

    @RequestMapping(
            path= {"/superapp/miniapp/{miniAppName}"},
            method = {RequestMethod.POST},
            produces = {MediaType.APPLICATION_JSON_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    public Object invokeMiniappCommand (@RequestBody CommandBoundary command,
                                        @PathVariable("miniAppName") String miniappName)
    {
        command.setInvocationTimestamp(new Date());
        return command;
    }
}
