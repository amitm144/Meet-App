package com.superapp.controllers;

import com.superapp.boundaries.command.CommandBoundary;
import com.superapp.boundaries.command.CommandIdBoundary;
import com.superapp.boundaries.object.ObjectIdBoundary;
import com.superapp.boundaries.user.UserBoundary;
import com.superapp.logic.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class AdminController {

    private UsersService usersService;

    @Autowired
    public void setMessageService(UsersService usersService) {
        this.usersService = usersService;
    }

    @RequestMapping(
            path= {"/superapp/admin/users"},
            method = {RequestMethod.GET},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public UserBoundary[] getAllUsers () {
        List<UserBoundary> l = this.usersService.getAllUsers();
        return l.toArray(new UserBoundary[0]);
    }

    @RequestMapping(
            path= {"/superapp/admin/miniapp"},
            method = {RequestMethod.GET},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public CommandBoundary[] exportMiniappsCommands () { return getNcommandBoundries(5); }

    @RequestMapping(
            path= {"/superapp/admin/miniapp/{miniAppName}"},
            method = {RequestMethod.GET},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public CommandBoundary[] exportSpecificMiniappsCommands(@PathVariable("miniAppName") String miniappName) {
        CommandBoundary[] c = getNcommandBoundries(1);
        CommandIdBoundary b = c[0].getCommandId();
        b.setMiniapp(miniappName);
        c[0].setCommandId(b);
        return c;
    }

    @RequestMapping(
                path= {"/superapp/admin/users"},
                method = {RequestMethod.DELETE})
    public void deleteUsers () { this.usersService.deleteAllUsers(); }

    @RequestMapping(
            path= {"/superapp/admin/objects"},
            method = {RequestMethod.DELETE})
    public void deleteObjects () {}

    @RequestMapping(
            path= {"/superapp/admin/miniapp"},
            method = {RequestMethod.DELETE})
    public void deleteMiniApp () {}

    private UserBoundary[] getNRandomUsers(int n) {
        UserBoundary[] userBoundaries = new UserBoundary[n];
        for (int i = 0; i < n; i++) {
            userBoundaries[i] = new UserBoundary(
                    String.format("random%d@example.com", i),"example",
                    String.format("random%d", i), String.format("%d", i));
        }
        return userBoundaries;
    }

    private CommandBoundary[] getNcommandBoundries(int n ){
        Map<String,Object> commandAttributes;
        String commandName = "CommandName num :";
        CommandBoundary[] commandArray = new CommandBoundary[n];
        for(int i=0; i<n ;i++){
            commandAttributes = new HashMap<String,Object>();
            commandAttributes.put("key "+i,i);
            UserBoundary user = getNRandomUsers(1)[0];
            commandArray[i] = new CommandBoundary(new CommandIdBoundary("mini :" + i),
                    commandName+i,new ObjectIdBoundary(),user.getUserId(),commandAttributes);
        }
        return commandArray;
    }
}
