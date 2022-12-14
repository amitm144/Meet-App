package superapp.boundaries.user;

public class UserIdBoundary {

    private String superapp;
    private String email;

    public UserIdBoundary() {}

    public UserIdBoundary(String email) { this.email = email; }

    public UserIdBoundary(String superapp, String email) {
        this.superapp = superapp;
        this.email = email;
    }

    public String getSuperapp() { return superapp; }

    public void setSuperapp(String superapp) {
        this.superapp = superapp;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "UserIdBoundary{" +
                "superapp='" + superapp + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserIdBoundary that = (UserIdBoundary)o;
        return this.superapp.equals(that.getSuperapp()) && this.email.equals(that.getEmail());
    }
}
