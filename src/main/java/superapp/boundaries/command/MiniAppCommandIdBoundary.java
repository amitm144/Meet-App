package superapp.boundaries.command;

public class MiniAppCommandIdBoundary {

    private String superapp;
    private String miniapp;
    private String internalCommandId;

    public MiniAppCommandIdBoundary() {}

    public MiniAppCommandIdBoundary(String miniApp, String internalCommandId) {
        this.miniapp = miniApp;
        this.internalCommandId = internalCommandId;
    }

    public MiniAppCommandIdBoundary(String miniApp) {
        this.miniapp = miniapp;
    }

    public String getSuperapp() {
        return superapp;
    }

    public void setSuperapp(String superapp) {
        this.superapp = superapp;
    }

    public String getMiniapp() {
        return miniapp;
    }

    public void setMiniapp(String miniapp) {
        this.miniapp = miniapp;
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
                "superapp='" + superapp + '\'' +
                ", miniapp='" + miniapp + '\'' +
                ", internalCommandId='" + internalCommandId + '\'' +
                '}';
    }
}
