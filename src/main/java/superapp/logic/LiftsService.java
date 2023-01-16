package superapp.logic;

import superapp.boundaries.user.UserIdBoundary;
import superapp.data.SuperAppObjectEntity;
import superapp.data.SuperappObjectPK;

public interface LiftsService {
    public Object startDrive(SuperAppObjectEntity drive);
    public void approveLiftRequest(SuperAppObjectEntity drive, UserIdBoundary requestingUser);
    public void rejectLiftRequest(SuperAppObjectEntity drive, UserIdBoundary requestingUser);
}
