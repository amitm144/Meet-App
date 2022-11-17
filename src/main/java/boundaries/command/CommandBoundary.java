package boundaries.command;

import boundaries.user.UserBoundary;

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

    public CommandBoundary() {}

    public CommandBoundary(CommandIdBoundary commandId, String command,
                           Map<String, Object> targetObject, Map<String, Object> invokedBy,
                           Map<String, Object> commandAttributes)
    {
        this.commandId = commandId;
        this.command = command;
        this.targetObject = targetObject;
        this.invocationTimeStamp = new Date();
        this.invokedBy = invokedBy;
        this.commandAttributes = commandAttributes;
    }
    public static CommandBoundary[] getNcommandBoundries(int n ){
        Map<String,Object> tObject;
        Map<String, Object> invokedBy;
        Map<String,Object> commandAttributes;

        String commandName = "CommandName num :";
        CommandBoundary[] commandArray = new CommandBoundary[n];
        for(int i=0; i<n ;i++){
            tObject = new HashMap<String,Object>();
            tObject.put("TargetObject",new ObjectIdBoundary());
            invokedBy = new HashMap<String,Object>();
            invokedBy.put(commandName,UserBoundary.getNRandomUsers(1)[0].getUserId());
            commandAttributes = new HashMap<String,Object>();
            commandAttributes.put("key "+i,i);
            commandArray[i] = new CommandBoundary(new CommandIdBoundary("miniApp :"+i),commandName+i,tObject,invokedBy,commandAttributes);
        }

        return commandArray;
    }

    public CommandIdBoundary getCommandId() {
        return commandId;
    }

    public void setCommandId(CommandIdBoundary commandId) {
        this.commandId = commandId;
    }

    public Map<String, Object> getTargetObject() {
        return targetObject;
    }

    public void setTargetObject(Map<String, Object> targetObject) {
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

    public Map<String, Object> getInvokedBy() {
        return invokedBy;
    }

    public void setInvokedBy(Map<String, Object> invokedBy) {
        this.invokedBy = invokedBy;
    }

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
                ", invocationTimeStamp=" + invocationTimeStamp +
                ", invokedBy=" + invokedBy +
                ", commandAttributes=" + commandAttributes +
                '}';
    }

}
