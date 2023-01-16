package superapp.logic;

import superapp.boundaries.user.UserIdBoundary;
import superapp.data.SuperappObjectPK;

public interface LiftsService {
    public Object startDrive(SuperappObjectPK drive);
    public void approveLiftRequest(SuperappObjectPK drive, UserIdBoundary requestingUser);
    public void rejectLiftRequest(SuperappObjectPK drive, UserIdBoundary requestingUser);
}
