package com.superapp.boundaries.data.split.Group;

import com.superapp.boundaries.user.UserBoundary;

import java.util.List;

public class GroupBoundary {
    private UserBoundary groupLeader;
    private List<UserBoundary> group;
    private String avatar;

    public GroupBoundary(){};

    public GroupBoundary(UserBoundary groupLeader, List<UserBoundary> group, String avatar) {
        this.groupLeader = groupLeader;
        this.group = group;
        this.avatar = avatar;
    }


    public UserBoundary getGroupLeader() {
        return groupLeader;
    }

    public void setGroupLeader(UserBoundary groupLeader) {
        this.groupLeader = groupLeader;
    }

    public List<UserBoundary> getGroup() {
        return group;
    }

    public void setGroup(List<UserBoundary> group) {
        this.group = group;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

}
