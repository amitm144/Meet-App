package controllers;

import boundaries.command.CommandBoundary;
import boundaries.command.CommandIdBoundary;
import boundaries.command.ObjectIdBoundary;
import boundaries.user.UserIdBoundary;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

@RestController
public class MiniAppObjectsController {

    @RequestMapping(
            path= {"/superapp/miniapp/{miniAppName}"},
            method = {RequestMethod.POST},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public Object invokeMiniAppCommand (@RequestBody CommandBoundary command,
                                        @PathVariable("miniAppName") String miniAppName)
    {
        command.setInvocationTimeStamp(new Date());
        return command;
    }
}
