package com.superapp.logic;

import com.superapp.data.GroupEntity;
import com.superapp.data.UserEntity;
import com.superapp.data.split.SplitGroupEntity;

public interface SplitsService {
   void openNewSplitGroup(GroupEntity group, String title);
    void openNewTransaction(GroupEntity group, UserEntity payedUser, String description, double splitbalance);
    void payDebt(GroupEntity group,UserEntity user);
    double showDebt(GroupEntity group,UserEntity user);
    void computeTransacionBalance(SplitGroupEntity group);

}
