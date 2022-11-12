package controllers;

import boundaries.command.CommandBoundary;
import boundaries.command.CommandIdBoundary;
import boundaries.command.ObjectIdBoundary;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
public class MiniAppObjectsController {

    @RequestMapping(
            path= {"/superapp/miniapp/{miniAppName}"},
            method = {RequestMethod.POST},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public CommandBoundary invokeMiniAppCommand (@RequestBody CommandBoundary command,
                                                 @PathVariable("miniAppName") String miniAppName)
    {
        CommandIdBoundary id = command.getCommandId();
        ObjectIdBoundary objectId = command.getTargetObject();
        String cmd = command.getCommand();
        Map<String,Object> attributes = command.getCommandAttributes();

        return new CommandBoundary(id,objectId,cmd,attributes);
    }
}
