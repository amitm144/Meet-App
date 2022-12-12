package com.superapp.boundaries.data.split;

import com.superapp.boundaries.data.Group.GroupEntity;
import com.superapp.boundaries.data.UserEntity;

public class SplitController implements SplitCommand{
    @Override
    public void openNewSplitGroup(GroupEntity group, String title) {

    }

    @Override
    public void openNewTrasnaction(GroupEntity group, UserEntity payedUser, String description, double splitbalance) {

    }

    @Override
    public void payDebt(GroupEntity group, UserEntity user) {

    }

    @Override
    public double showDebt(GroupEntity group, UserEntity user) {
        return 0;
    }
}
