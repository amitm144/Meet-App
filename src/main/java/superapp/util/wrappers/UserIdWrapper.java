package superapp.util.wrappers;

import superapp.boundaries.user.UserIdBoundary;

public class UserIdWrapper {

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
