package superapp.boundaries.split;

import superapp.boundaries.object.SuperAppObjectIdBoundary;
import superapp.boundaries.user.UserBoundary;

import java.util.List;

public class GroupBoundary {
    private SuperAppObjectIdBoundary groupId;
    private UserBoundary groupLeader;
    private List<UserBoundary> allUsers;
    private String avatar;


    public GroupBoundary() {
    }


    public GroupBoundary(SuperAppObjectIdBoundary groupId,
                         UserBoundary groupLeader,
                         List<UserBoundary> allUsers,
                         String avatar) {
        this.groupId = groupId; //todo
        this.groupLeader = groupLeader;
        this.allUsers = allUsers;
        this.avatar = avatar;
    }
    
    public UserBoundary getGroupLeader() {
        return groupLeader;
    }

    public void setGroupLeader(UserBoundary groupLeader) {
        this.groupLeader = groupLeader;
    }

    public List<UserBoundary> getAllUsers() {
        return allUsers;
    }

    public void setAllUsers(List<UserBoundary> allUsers) {
        this.allUsers = allUsers;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public SuperAppObjectIdBoundary getGroupId() {
        return groupId;
    }

    public void setGroupId(SuperAppObjectIdBoundary groupId) {
        this.groupId = groupId;
    }
}