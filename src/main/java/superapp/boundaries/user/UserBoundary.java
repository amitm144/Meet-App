package superapp.boundaries.user;

public class UserBoundary {

    private UserIdBoundary userId;
    private String role;
    private String username;
    private String avatar;

    public UserBoundary() {}

    public UserBoundary(String email, String role, String username, String avatar) {
        if (username == null || role == null || username.isBlank() || role.isBlank())
            throw new RuntimeException("Username or role cannot be blank");
        this.userId = new UserIdBoundary(email);
        this.role = role;
        this.username = username;
        this.avatar = avatar;
    }

    public UserBoundary(UserIdBoundary userId, String role, String username, String avatar) {
        if (username == null || role == null || username.isBlank() || role.isBlank())
            throw new RuntimeException("Username or role cannot be blank");
        this.userId = userId;
        this.role = role;
        this.username = username;
        this.avatar = avatar;
    }

    public UserBoundary(String superapp , String email, String role, String username, String avatar) {
        this(email, role, username, avatar);
        this.userId = new UserIdBoundary(superapp ,email);
    }

    public UserIdBoundary getUserId() {
        return userId;
    }

    public void setUserId(UserIdBoundary userId) {
        this.userId = userId;
    }

    public void setSuperApp(String superApp) {
        if (this.userId == null)
            this.userId = new UserIdBoundary();

        this.userId.setSuperapp(superApp);
    }

    public void setEmail(String email) {
        if (this.userId == null)
            this.userId = new UserIdBoundary(email);
        else
            this.userId.setEmail(email);
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    @Override
    public String toString() {
        return "UserBoundary {" +
                "userId=" + userId.toString() +
                ", role='" + role + '\'' +
                ", username='" + username + '\'' +
                ", avatar='" + avatar + '\'' +
                '}';
    }
}
