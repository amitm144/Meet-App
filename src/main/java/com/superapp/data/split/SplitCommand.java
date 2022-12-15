package com.superapp.boundaries.data.split;

import com.superapp.boundaries.data.Group.GroupEntity;
import com.superapp.boundaries.data.UserEntity;

public interface SplitCommand {
   void openNewSplitGroup(GroupEntity group, String title);
    void openNewTrasnaction(GroupEntity group, UserEntity payedUser, String description, double splitbalance);
    void payDebt(GroupEntity group,UserEntity user);
    double showDebt(GroupEntity group,UserEntity user);

}
