package com.superapp.data.split;

import com.superapp.data.Group.GroupEntity;
import com.superapp.data.UserEntity;

public interface SplitCommand {
   void openNewSplitGroup(GroupEntity group, String title);
    void openNewTrasnaction(GroupEntity group, UserEntity payedUser, String description, double splitbalance);
    void payDebt(GroupEntity group,UserEntity user);
    double showDebt(GroupEntity group,UserEntity user);

}
