package com.superapp.boundaries.data.split.Group;

import com.superapp.boundaries.user.UserBoundary;

public class GroupIdBoundary {

    private String superapp ;
    private UserBoundary leader ;

    public GroupIdBoundary(String superapp, UserBoundary leader) {
        this.superapp = superapp;
        this.leader = leader;
    }

    public String getSuperapp() {
        return superapp;
    }

    public void setSuperapp(String superapp) {
        this.superapp = superapp;
    }

    public UserBoundary getLeader() {
        return leader;
    }

    public void setLeader(UserBoundary leader) {
        this.leader = leader;
    }
}
