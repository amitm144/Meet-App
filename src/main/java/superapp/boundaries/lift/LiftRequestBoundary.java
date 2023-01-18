package superapp.boundaries.lift;

import superapp.boundaries.user.UserIdBoundary;

public class LiftRequestBoundary {
    private UserIdBoundary userId;
    private String origin;

    public LiftRequestBoundary() {}

    public LiftRequestBoundary(UserIdBoundary userId, String origin) {
        this.userId = userId;
        this.origin = origin;
    }

    public UserIdBoundary getUserId() {
        return userId;
    }

    public void setUserId(UserIdBoundary userId) {
        this.userId = userId;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    @Override
    public String toString() {
        return "LiftRequestBoundary{" +
                "userId=" + userId +
                ", origin='" + origin + '\'' +
                '}';
    }
}
