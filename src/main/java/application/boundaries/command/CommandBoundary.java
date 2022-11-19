package application.boundaries.command;

import application.boundaries.user.UserIdBoundary;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CommandBoundary {
    /*
        targetObject and invokedBy are maps in order to handle the requested data structure properly.
     */
    private CommandIdBoundary commandId ;
    private String command;
    private Map<String, Object> targetObject;
    private Date invocationTimeStamp;
    private Map<String, Object> invokedBy;
    private Map<String, Object> commandAttributes;

    public CommandBoundary() {
        this.targetObject = new HashMap<>();
        this.invokedBy = new HashMap<>();
    }

    public CommandBoundary(CommandIdBoundary commandId, String command,
                           ObjectIdBoundary targetObject, UserIdBoundary invokedBy,
                           Map<String, Object> commandAttributes)
    {
        this();
        this.commandId = commandId;
        this.command = command;
        this.commandAttributes = commandAttributes;
        this.invocationTimeStamp = new Date();
        this.targetObject.put("ObjectId", targetObject);
        this.invokedBy.put("UserId", invokedBy);
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

    public void setTargetObject(ObjectIdBoundary targetObject) {
        this.targetObject.put("ObjectId", targetObject);
    }

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

    public void setInvokedBy(UserIdBoundary invokedBy) {
        this.invokedBy.put("UserId", invokedBy);
    }

    public Object getCommandAttributes() {
        return commandAttributes;
    }

    public void setCommandAttributes(Map<String, Object> commandAttributes) {
        this.commandAttributes = commandAttributes;
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
