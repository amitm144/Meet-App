package com.superapp.data;

import com.superapp.boundaries.object.ObjectIdBoundary;
import com.superapp.data.UserEntity;

import java.util.List;

public class GroupEntity {

    private String groupId;
    private String superapp;
    private UserEntity groupLeader;
    private List<UserEntity> allUsers ;
    private String avatar;

    public GroupEntity() {
    }

    public GroupEntity(String groupId, UserEntity groupLeader, List<UserEntity> allUsers, String avatar) {
        this.groupId = groupId; //todo
        this.groupLeader = groupLeader;
        this.allUsers = allUsers;
        this.avatar = avatar;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public UserEntity getGroupLeader() {
        return groupLeader;
    }

    public void setGroupLeader(UserEntity groupLeader) {
        this.groupLeader = groupLeader;
    }

    public List<UserEntity> getAllUsers() {
        return allUsers;
    }

    public void setAllUsers(List<UserEntity> allUsers) {
        this.allUsers = allUsers;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getSuperapp() {
        return superapp;
    }

    public void setSuperapp(String superapp) {
        this.superapp = superapp;
    }
}
