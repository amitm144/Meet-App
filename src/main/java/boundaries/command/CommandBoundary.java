package boundaries.command;

import java.util.Date;
import java.util.Map;


public class CommandBoundary {

    private CommandIdBoundary commandId ;
    private ObjectIdBoundary targetObject;
    private String command ;
    private Date invocationTimeStamp;
    private Map<String, Object> commandAttributes;

    public CommandBoundary() {}

    public CommandBoundary(CommandIdBoundary commandId, ObjectIdBoundary targetObject, String command, Map<String, Object> commandAttributes) {
        this.commandId = commandId;
        this.targetObject = targetObject;
        this.command = command;
        this.invocationTimeStamp = new Date();
        this.commandAttributes = commandAttributes;
    }

    public CommandIdBoundary getCommandId() {
        return commandId;
    }

    public void setCommandId(CommandIdBoundary commandId) {
        this.commandId = commandId;
    }

    public ObjectIdBoundary getTargetObject() {
        return targetObject;
    }

    public void setTargetObject(ObjectIdBoundary targetObject) {
        this.targetObject = targetObject;
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

    public Map<String, Object> getCommandAttributes() {
        return commandAttributes;
    }

    public void setCommandAttributes(Map<String, Object> commandAttributes) {
        this.commandAttributes = commandAttributes;
    }
}
