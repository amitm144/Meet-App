package boundaries;

public class UserIdBoundary {

    private String superApp ;
    private String email ;

    public UserIdBoundary() {
        this.superApp = "2023a.noam.levy";
    }

    public UserIdBoundary(String superApp, String email) {
        this.superApp = superApp;
        this.email = email;
    }

    public UserIdBoundary(String email) {
        super();
        this.email = email;
    }

    public String getSuperApp() {
        return superApp;
    }

    public void setSuperApp(String superApp) {
        this.superApp = superApp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
