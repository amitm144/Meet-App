package com.superapp.boundaries.command;

import com.superapp.boundaries.object.ObjectIdBoundary;
import com.superapp.boundaries.user.UserBoundary;
import com.superapp.boundaries.user.UserIdBoundary;
import com.superapp.util.wrappers.ObjectIdWrapper;
import com.superapp.util.wrappers.UserIdWrapper;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CommandBoundary {
    private CommandIdBoundary commandId ;
    private String command;
    private ObjectIdWrapper targetObject;
    private Date invocationTimestamp;
    private UserIdWrapper invokedBy;
    private Map<String, Object> commandAttributes;

    public CommandBoundary() { }

    public CommandBoundary(CommandIdBoundary commandId, String command,
                           ObjectIdBoundary targetObject, UserIdBoundary invokedBy,
                           Map<String, Object> commandAttributes)
    {
        this();
        this.commandId = commandId;
        this.command = command;
        this.commandAttributes = commandAttributes;
        this.invocationTimestamp = new Date();
        this.targetObject = new ObjectIdWrapper(targetObject);
        this.invokedBy = new UserIdWrapper(invokedBy);
    }
    public CommandIdBoundary getCommandId() {
        return commandId;
    }

    public void setCommandId(CommandIdBoundary commandId) {
        this.commandId = commandId;
    }

    public Object getTargetObject() {
        return targetObject;
    }

    public void setTargetObject(ObjectIdWrapper targetObject) { this.targetObject = targetObject; }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public Date getInvocationTimestamp() {
        return invocationTimestamp;
    }

    public void setInvocationTimestamp(Date invocationTimestamp) {
        this.invocationTimestamp = invocationTimestamp;
    }

    public Object getInvokedBy() {
        return invokedBy;
    }

    public void setInvokedBy(UserIdWrapper invokedBy) { this.invokedBy = invokedBy; }

    public Object getCommandAttributes() {
        return commandAttributes;
    }

    public void setCommandAttributes(Map<String, Object> commandAttributes) {
        this.commandAttributes = commandAttributes;
    }
    public static CommandBoundary[] getNcommandBoundries(int n ){
        Map<String,Object> commandAttributes;
        String commandName = "CommandName num :";
        CommandBoundary[] commandArray = new CommandBoundary[n];
        for(int i=0; i<n ;i++){
            commandAttributes = new HashMap<String,Object>();
            commandAttributes.put("key "+i,i);
            UserBoundary user = UserBoundary.getNRandomUsers(1)[0];
            commandArray[i] = new CommandBoundary(new CommandIdBoundary("mini :" + i),
                    commandName+i,new ObjectIdBoundary(),user.getUserId(),commandAttributes);
        }
        return commandArray;
    }
    @Override
    public String toString() {
        return "CommandBoundary{" +
                "command='" + command + '\'' +
                ", targetObject=" + targetObject +
                ", invocationTimestamp=" + invocationTimestamp +
                ", invokedBy=" + invokedBy +
                ", commandAttributes=" + commandAttributes +
                '}';
    }
}
