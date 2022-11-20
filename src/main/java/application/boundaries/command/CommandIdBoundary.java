package application.boundaries.command;

import java.util.Random;

public class CommandIdBoundary {

    private String superApp ;
    private String miniApp ;
    private String internalCommandId;

    public CommandIdBoundary() {
        this.superApp = "2023a.noam.levy";
        int id = new Random().nextInt(1000);
        this.internalCommandId = Integer.toString(id);
    }

    public CommandIdBoundary(String miniApp, String internalCommandId) {
        if (miniApp.isBlank() || internalCommandId.isBlank())
            throw new RuntimeException("command id or miniApp name cannot be blank");
        this.miniApp = miniApp;
        this.internalCommandId = internalCommandId;
    }

    public CommandIdBoundary(String miniApp) {
        this();
        this.miniApp = miniApp;
    }

    public String getSuperApp() {
        return superApp;
    }

    public void setSuperApp(String superApp) {
        this.superApp = superApp;
    }

    public String getMiniApp() {
        return miniApp;
    }

    public void setMiniApp(String miniApp) {
        this.miniApp = miniApp;
    }

    public String getInternalCommandId() {
        return internalCommandId;
    }

    public void setInternalCommandId(String internalCommandId) {
        this.internalCommandId = internalCommandId;
    }

    @Override
    public String toString() {
        return "CommandIdBoundary{" +
                "superApp='" + superApp + '\'' +
                ", miniApp='" + miniApp + '\'' +
                ", internalCommandId='" + internalCommandId + '\'' +
                '}';
    }
}