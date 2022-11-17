package boundaries.user;


import application.EmailChecker;

public class UserIdBoundary {

    private String superApp ;
    private String email ;

    public UserIdBoundary() {
        this.superApp = "2023a.noam.levy";
    }

    public UserIdBoundary(String email) {
        this();
        if (!EmailChecker.isValidEmail(email))
            throw new RuntimeException("invalid email");
        this.email = email;
    }

    public UserIdBoundary(String superApp, String email) {
        if (!EmailChecker.isValidEmail(email))
            throw new RuntimeException("invalid email");
        this.superApp = superApp;
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
        if (EmailChecker.isValidEmail(email))
            this.email = email;
    }

    @Override
    public String toString() {
        return "UserIdBoundary{" +
                "superApp='" + superApp + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
