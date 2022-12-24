
package superapp.data.Group;

import superapp.data.GroupEntity;
import superapp.data.UserEntity;
import superapp.data.split.SplitTransaction;

import java.util.ArrayList;
import java.util.List;

public class SplitGroupEntity {

    private String groupId;
    private String superapp;
    private UserEntity groupLeader;
    private List<UserEntity> allUsers;
    private String avatar;
    private ArrayList<SplitTransaction> expenses;
    private String SplitTitle;

    public SplitGroupEntity() {
    }

    public SplitGroupEntity(GroupEntity group, String splitTitle) {
        this.groupId = group.getGroupId(); // todo
        this.allUsers= group.getAllUsers();
        this.avatar= group.getAvatar();
        this.superapp= group.getSuperapp();
        this.expenses = new ArrayList<SplitTransaction> ();
        SplitTitle = splitTitle;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public ArrayList<SplitTransaction> getExpenses() {
        return expenses;
    }

    public void setExpenses(ArrayList<SplitTransaction> balances) {
        this.expenses = balances;
    }

    public String getSplitTitle() {
        return SplitTitle;
    }

    public void setSplitTitle(String splitTitle) {
        SplitTitle = splitTitle;
    }


    public void addNewTransaction(SplitTransaction newTran) {
        this.expenses.add(newTran);
    }

    public String getSuperapp() {
        return superapp;
    }

    public void setSuperapp(String superapp) {
        this.superapp = superapp;
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

    public GroupEntity getGroup(){//    public GroupEntity(String groupId, UserEntity groupLeader, List<UserEntity> allUsers, String avatar) {
        return new GroupEntity(this.groupId,this.groupLeader,this.allUsers,this.avatar);
    }
}
