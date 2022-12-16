package com.superapp.boundaries.split;

import com.superapp.boundaries.object.ObjectIdBoundary;
import com.superapp.boundaries.user.UserBoundary;

import java.util.List;

public class GroupBoundary {
    private ObjectIdBoundary groupId;
    private UserBoundary groupLeader;
    private List<UserBoundary> allUsers;
    private String avatar;


    public GroupBoundary() {
    }


    public GroupBoundary(ObjectIdBoundary groupId, UserBoundary groupLeader, List<UserBoundary> allUsers, String avatar) {
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

    public ObjectIdBoundary getGroupId() {
        return groupId;
    }

    public void setGroupId(ObjectIdBoundary groupId) {
        this.groupId = groupId;
    }
}