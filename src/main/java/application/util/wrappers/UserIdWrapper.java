package application.util.wrappers;

import application.boundaries.user.UserIdBoundary;
//import com.fasterxml.jackson.annotation.JsonTypeName;

//@JsonTypeName("userId")
public class UserIdWrapper /*implements ObjectWrapper*/ {

    public UserIdBoundary userId;

    public UserIdWrapper() {}

    public UserIdWrapper(UserIdBoundary userId) { this.userId = userId; }

    public UserIdBoundary getUserId() { return userId; }

    public void setUserId(UserIdBoundary userId) { this.userId = userId; }

    @Override
    public String toString() {
        return "UserIdWrapper{" +
                "userId=" + userId +
                '}';
    }
}
