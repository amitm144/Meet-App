package boundaries.user;

public class UserBoundary {

    private UserIdBoundary userId;
    private String role ;
    private String username ;
    private String avatar ;

    public UserBoundary(){
    }

    public UserBoundary(String superapp , String email, String role, String username, String avatar) {
        this.userId = new UserIdBoundary(superapp ,email);
        this.role = role;
        this.username = username;
        this.avatar = avatar;
    }
    public UserBoundary(String email, String role, String username, String avatar) {
        this.userId = new UserIdBoundary(email);
        this.role = role;
        this.username = username;
        this.avatar = avatar;
    }


    public static UserBoundary[] getNRandomUsers(int n){
        UserBoundary[] userBoundaries = new UserBoundary[n];
        for (int i = 0; i < n; i++) {
            userBoundaries[i] = new UserBoundary(String.format("random%d@example.com", i) ,
                    "example" , String.format("random%d", i) , String.format("%d", i));
        }
        return userBoundaries;
    }

    public UserIdBoundary getUserId() {
        return userId;
    }

    public void setUserId(UserIdBoundary userId) {
        this.userId = userId;
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
}
