package com.superapp.boundaries.data.split.Group;

import com.superapp.boundaries.data.UserEntity;

import java.util.List;

public class GroupEntity {

    private String groupId;
    private List<UserEntity> members ;

    public GroupEntity(String groupId, List<UserEntity> members) {
        this.groupId = groupId;
        this.members = members;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public List<UserEntity> getMembers() {
        return members;
    }

    public void setMembers(List<UserEntity> members) {
        this.members = members;
    }
}
