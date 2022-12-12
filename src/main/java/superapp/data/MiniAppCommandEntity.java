package superapp.data;
import java.util.Date;

public class MiniAppCommandEntity {

    private String superapp ;
    private String miniapp ;
    private String internalCommandId;
    private String command;
    private String internalObjectId; // ObjectID
    private Date invocationTimestamp;
    private String email ; // USERID
    private String commandAttributes;

    public MiniAppCommandEntity(){
    }
    public MiniAppCommandEntity(String superApp, String miniApp, String internalCommandId, String command, String internalObjectId, Date invocationTimeStamp, String email, String commandAttributes) {
        this();
        this.superapp = superApp;
        this.miniapp = miniApp;
        this.internalCommandId = internalCommandId;
        this.command = command;
        this.internalObjectId = internalObjectId;
        this.invocationTimestamp = invocationTimeStamp;
        this.email = email;
        this.commandAttributes = commandAttributes;
    }

    public void setSuperApp(String superApp) {
        this.superapp = superApp;
    }

    public void setMiniApp(String miniApp) {
        this.miniapp = miniApp;
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

    public void setInvocationTimestamp(Date invocationTimestamp) {
        this.invocationTimestamp = invocationTimestamp;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCommandAttributes(String commandAttributes) {
        this.commandAttributes = commandAttributes;
    }

    public String getSuperApp() {
        return superapp;
    }

    public String getMiniApp() {
        return miniapp;
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

    public Date getInvocationTimestamp() {
        return invocationTimestamp;
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
                "superApp='" + superapp + '\'' +
                ", miniApp='" + miniapp + '\'' +
                ", internalCommandId='" + internalCommandId + '\'' +
                ", command='" + command + '\'' +
                ", internalObjectId='" + internalObjectId + '\'' +
                ", invocationTimeStamp=" + invocationTimestamp +
                ", email='" + email + '\'' +
                ", commandAttributes=" + commandAttributes +
                '}';
    }
}
