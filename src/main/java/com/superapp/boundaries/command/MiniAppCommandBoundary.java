package com.superapp.boundaries.command;

import com.superapp.boundaries.object.ObjectIdBoundary;
import com.superapp.boundaries.user.UserBoundary;
import com.superapp.boundaries.user.UserIdBoundary;
import com.superapp.util.wrappers.ObjectIdWrapper;
import com.superapp.util.wrappers.UserIdWrapper;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MiniAppCommandBoundary {
    private MiniAppCommandIdBoundary commandId ;
    private String command;
    private ObjectIdWrapper targetObject;
    private Date invocationTimeStamp;
    private UserIdWrapper invokedBy;
    private Map<String, Object> commandAttributes;

    public MiniAppCommandBoundary() {

    }

    public MiniAppCommandBoundary(MiniAppCommandIdBoundary commandId, String command,
                                  ObjectIdBoundary targetObject, UserIdBoundary invokedBy,
                                  Map<String, Object> commandAttributes)
    {
        this();
        this.commandId = commandId;
        this.command = command;
        this.commandAttributes = commandAttributes;
        this.invocationTimeStamp = new Date();
        this.targetObject = new ObjectIdWrapper(targetObject);
        this.invokedBy = new UserIdWrapper(invokedBy);
    }
    public MiniAppCommandIdBoundary getCommandId() {
        return commandId;
    }

    public void setCommandId(MiniAppCommandIdBoundary commandId) {
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

    public Date getInvocationTimeStamp() {
        return invocationTimeStamp;
    }

    public void setInvocationTimeStamp(Date invocationTimeStamp) {
        this.invocationTimeStamp = invocationTimeStamp;
    }

    public Object getInvokedBy() {
        return invokedBy;
    }

    public void setInvokedBy(UserIdWrapper invokedBy) { this.invokedBy = invokedBy; }

    public Map<String, Object> getCommandAttributes() {
        return commandAttributes;
    }

    public void setCommandAttributes(Map<String, Object> commandAttributes) {
        this.commandAttributes = commandAttributes;
    }
    public static MiniAppCommandBoundary[] getNcommandBoundries(int n ){
        Map<String,Object> commandAttributes;
        String commandName = "CommandName num :";
        MiniAppCommandBoundary[] commandArray = new MiniAppCommandBoundary[n];
        for(int i=0; i<n ;i++){
            commandAttributes = new HashMap<String,Object>();
            commandAttributes.put("key "+i,i);
            UserBoundary user = UserBoundary.getNRandomUsers(1)[0];
            commandArray[i] = new MiniAppCommandBoundary(new MiniAppCommandIdBoundary("mini :" + i),
                    commandName+i,new ObjectIdBoundary(),user.getUserId(),commandAttributes);
        }
        return commandArray;
    }
    @Override
    public String toString() {
        return "CommandBoundary{" +
                "command='" + command + '\'' +
                ", targetObject=" + targetObject +
                ", invocationTimeStamp=" + invocationTimeStamp +
                ", invokedBy=" + invokedBy +
                ", commandAttributes=" + commandAttributes +
                '}';
    }
}
