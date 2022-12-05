package com.superapp.data;
import java.util.Date;

public class MiniAppCommandEntity {
    private String superApp ;
    private String miniApp ;
    private String internalCommandId;
    private String command;
    private String internalObjectId; // ObjectID
    private Date invocationTimeStamp;
    private String email ; // USERID
    private String commandAttributes;

    public MiniAppCommandEntity(){
    }
    public MiniAppCommandEntity(String superApp, String miniApp, String internalCommandId, String command, String internalObjectId, Date invocationTimeStamp, String email, String commandAttributes) {
        this();
        this.superApp = superApp;
        this.miniApp = miniApp;
        this.internalCommandId = internalCommandId;
        this.command = command;
        this.internalObjectId = internalObjectId;
        this.invocationTimeStamp = invocationTimeStamp;
        this.email = email;
        this.commandAttributes = commandAttributes;
    }

    public void setSuperApp(String superApp) {
        this.superApp = superApp;
    }

    public void setMiniApp(String miniApp) {
        this.miniApp = miniApp;
    }

    public void setInternalCommandId(String internalCommandId) {
        this.internalCommandId = internalCommandId;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public void setInternalObjectId(String internalObjectId) {
        this.internalObjectId = internalObjectId;
    }

    public void setInvocationTimeStamp(Date invocationTimeStamp) {
        this.invocationTimeStamp = invocationTimeStamp;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCommandAttributes(String commandAttributes) {
        this.commandAttributes = commandAttributes;
    }

    public String getSuperApp() {
        return superApp;
    }

    public String getMiniApp() {
        return miniApp;
    }

    public String getInternalCommandId() {
        return internalCommandId;
    }

    public String getCommand() {
        return command;
    }

    public String getInternalObjectId() {
        return internalObjectId;
    }

    public Date getInvocationTimeStamp() {
        return invocationTimeStamp;
    }

    public String getEmail() {
        return email;
    }

    public String getCommandAttributes() {
        return commandAttributes;
    }

    @Override
    public String toString() {
        return "MiniAppCommandEntity{" +
                "superApp='" + superApp + '\'' +
                ", miniApp='" + miniApp + '\'' +
                ", internalCommandId='" + internalCommandId + '\'' +
                ", command='" + command + '\'' +
                ", internalObjectId='" + internalObjectId + '\'' +
                ", invocationTimeStamp=" + invocationTimeStamp +
                ", email='" + email + '\'' +
                ", commandAttributes=" + commandAttributes +
                '}';
    }
}
