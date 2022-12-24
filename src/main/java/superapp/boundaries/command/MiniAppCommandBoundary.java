package superapp.boundaries.command;

import superapp.boundaries.object.ObjectIdBoundary;
import superapp.boundaries.user.UserIdBoundary;
import superapp.util.wrappers.ObjectIdWrapper;
import superapp.util.wrappers.UserIdWrapper;

import java.util.Date;
import java.util.Map;

public class MiniAppCommandBoundary {
    private MiniAppCommandIdBoundary commandId ;
    private String command;
    private ObjectIdWrapper targetObject;
    private Date invocationTimestamp;
    private UserIdWrapper invokedBy;
    private Map<String, Object> commandAttributes;

    public MiniAppCommandBoundary() {}

    public MiniAppCommandBoundary(MiniAppCommandIdBoundary commandId, String command,
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

    public MiniAppCommandIdBoundary getCommandId() {
        return commandId;
    }

    public void setCommandId(MiniAppCommandIdBoundary commandId) {
        this.commandId = commandId;
    }

    public ObjectIdWrapper getTargetObject() { return targetObject; }

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

    public void setInvocationTimestamp(Date invocationTimeStamp) {
        this.invocationTimestamp = invocationTimeStamp;
    }

    public UserIdWrapper getInvokedBy() { return invokedBy; }

    public void setInvokedBy(UserIdWrapper invokedBy) { this.invokedBy = invokedBy; }

    public Map<String, Object> getCommandAttributes() {
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
                ", invocationTimeStamp=" + invocationTimestamp +
                ", invokedBy=" + invokedBy +
                ", commandAttributes=" + commandAttributes +
                '}';
    }
}
