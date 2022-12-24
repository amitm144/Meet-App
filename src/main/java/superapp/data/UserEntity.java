package superapp.data;


public class UserEntity {

    private String superapp;
    private String email;
    private String username;
    private UserRole role;
    private String avatar;
    private String firstName, lastName;

    public UserEntity() {}

    public UserEntity(String superapp, String email, String username,
                      UserRole role, String avatar,
                      String firstName, String lastName)
    {
        this.superapp = superapp;
        this.email = email;
        this.username = username;
        this.role = role;
        this.avatar = avatar;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getSuperapp() {
        return superapp;
    }

    public void setSuperapp(String superApp) {
        this.superapp = superApp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "superApp='" + superapp + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", role=" + role +
                ", avatar='" + avatar + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
