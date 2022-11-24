package application.controllers;

import application.boundaries.command.CommandBoundary;
import application.boundaries.command.CommandIdBoundary;
import application.boundaries.user.UserBoundary;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
public class AdminController {
    @RequestMapping(
            path= {"/superapp/admin/users"},
            method = {RequestMethod.GET},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public UserBoundary[] createUsers () { return UserBoundary.getNRandomUsers(5); }
    @RequestMapping(
            path= {"/superapp/admin/miniapp"},
            method = {RequestMethod.GET},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public CommandBoundary[] exportMiniAppsCommands () { return CommandBoundary.getNcommandBoundries(5); }
    @RequestMapping(
            path= {"/superapp/admin/miniapp/{miniAppName}"},
            method = {RequestMethod.GET},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public CommandBoundary[] exportSpecificMiniAppsCommands(@PathVariable("miniAppName") String miniAppName) {
        CommandBoundary[] c = CommandBoundary.getNcommandBoundries(1);
        CommandIdBoundary b = c[0].getCommandId();
        b.setMiniApp(miniAppName);
        c[0].setCommandId(b);
        return c;
    }
    @RequestMapping(
                path= {"/superapp/admin/users"},
                method = {RequestMethod.DELETE})
    public void deleteUsers () {}
    @RequestMapping(
            path= {"/superapp/admin/objects"},
            method = {RequestMethod.DELETE})
    public void deleteObjects () {}
    @RequestMapping(
            path= {"/superapp/admin/miniapp"},
            method = {RequestMethod.DELETE})
    public void deleteMiniApp () {}
}
